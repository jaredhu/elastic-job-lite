/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.cloud.scheduler.mesos;

import com.dangdang.ddframe.job.cloud.scheduler.ha.FrameworkIDService;
import com.dangdang.ddframe.job.cloud.scheduler.statistics.StatisticManager;
import com.dangdang.ddframe.job.context.TaskContext;
import com.dangdang.ddframe.job.event.JobEventBus;
import com.dangdang.ddframe.job.event.type.JobStatusTraceEvent;
import com.dangdang.ddframe.job.event.type.JobStatusTraceEvent.Source;
import com.dangdang.ddframe.job.event.type.JobStatusTraceEvent.State;
import com.netflix.fenzo.TaskScheduler;
import org.apache.mesos.Protos;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 作业云引擎.
 *
 * @author zhangliang
 */
public final class SchedulerEngine implements Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerEngine.class);
    
    private final TaskScheduler taskScheduler;
    
    private final FacadeService facadeService;
    
    private final JobEventBus jobEventBus;
    
    private final FrameworkIDService frameworkIDService;
    
    private final StatisticManager statisticManager;

    public SchedulerEngine(TaskScheduler taskScheduler, FacadeService facadeService, JobEventBus jobEventBus, FrameworkIDService frameworkIDService, StatisticManager statisticManager) {
        this.taskScheduler = taskScheduler;
        this.facadeService = facadeService;
        this.jobEventBus = jobEventBus;
        this.frameworkIDService = frameworkIDService;
        this.statisticManager = statisticManager;
    }

    @Override
    public void registered(final SchedulerDriver schedulerDriver, final Protos.FrameworkID frameworkID, final Protos.MasterInfo masterInfo) {
        logger.info("call registered");
        frameworkIDService.save(frameworkID.getValue());
        taskScheduler.expireAllLeases();
        MesosStateService.register(masterInfo.getHostname(), masterInfo.getPort());
    }
    
    @Override
    public void reregistered(final SchedulerDriver schedulerDriver, final Protos.MasterInfo masterInfo) {
        logger.info("call reregistered");
        taskScheduler.expireAllLeases();
        MesosStateService.register(masterInfo.getHostname(), masterInfo.getPort());
    }
    
    @Override
    public void resourceOffers(final SchedulerDriver schedulerDriver, final List<Protos.Offer> offers) {
        for (Protos.Offer offer: offers) {
            logger.trace("Adding offer {} from host {}", offer.getId(), offer.getHostname());
            LeasesQueue.getInstance().offer(offer);
        }
    }
    
    @Override
    public void offerRescinded(final SchedulerDriver schedulerDriver, final Protos.OfferID offerID) {
        logger.trace("call offerRescinded: {}", offerID);
        taskScheduler.expireLease(offerID.getValue());
    }
    
    @Override
    public void statusUpdate(final SchedulerDriver schedulerDriver, final Protos.TaskStatus taskStatus) {
        String taskId = taskStatus.getTaskId().getValue();
        TaskContext taskContext = TaskContext.from(taskId);
        String jobName = taskContext.getMetaInfo().getJobName();
        logger.trace("call statusUpdate task state is: {}, task id is: {}", taskStatus.getState(), taskId);
        jobEventBus.post(new JobStatusTraceEvent(jobName, taskContext.getId(), taskContext.getSlaveId(), Source.CLOUD_SCHEDULER, 
                taskContext.getType(), String.valueOf(taskContext.getMetaInfo().getShardingItems()), State.valueOf(taskStatus.getState().name()), taskStatus.getMessage()));
        switch (taskStatus.getState()) {
            case TASK_RUNNING:
                if (!facadeService.load(jobName).isPresent()) {
                    schedulerDriver.killTask(Protos.TaskID.newBuilder().setValue(taskId).build());
                }
                if ("BEGIN".equals(taskStatus.getMessage())) {
                    facadeService.updateDaemonStatus(taskContext, false);
                } else if ("COMPLETE".equals(taskStatus.getMessage())) {
                    facadeService.updateDaemonStatus(taskContext, true);
                    statisticManager.taskRunSuccessfully();
                }
                break;
            case TASK_FINISHED:
                facadeService.removeRunning(taskContext);
                unAssignTask(taskId);
                statisticManager.taskRunSuccessfully();
                break;
            case TASK_KILLED:
                logger.warn("task id is: {}, status is: {}, message is: {}, source is: {}", taskId, taskStatus.getState(), taskStatus.getMessage(), taskStatus.getSource());
                facadeService.removeRunning(taskContext);
                facadeService.addDaemonJobToReadyQueue(jobName);
                unAssignTask(taskId);
                break;
            case TASK_LOST:
            case TASK_DROPPED:
            case TASK_GONE:
            case TASK_GONE_BY_OPERATOR:
            case TASK_FAILED:
            case TASK_ERROR:
                logger.warn("task id is: {}, status is: {}, message is: {}, source is: {}", taskId, taskStatus.getState(), taskStatus.getMessage(), taskStatus.getSource());
                facadeService.removeRunning(taskContext);
                facadeService.recordFailoverTask(taskContext);
                unAssignTask(taskId);
                statisticManager.taskRunFailed();
                break;
            case TASK_UNKNOWN:
            case TASK_UNREACHABLE:
                logger.error("task id is: {}, status is: {}, message is: {}, source is: {}", taskId, taskStatus.getState(), taskStatus.getMessage(), taskStatus.getSource());
                statisticManager.taskRunFailed();
                break;
            default:
                break;
        }
    }
    
    private void unAssignTask(final String taskId) {
        String hostname = facadeService.popMapping(taskId);
        if (null != hostname) {
            taskScheduler.getTaskUnAssigner().call(TaskContext.getIdForUnassignedSlave(taskId), hostname);
        }
    }
    
    @Override
    public void frameworkMessage(final SchedulerDriver schedulerDriver, final Protos.ExecutorID executorID, final Protos.SlaveID slaveID, final byte[] bytes) {
        logger.trace("call frameworkMessage slaveID: {}, bytes: {}", slaveID, new String(bytes));
    }
    
    @Override
    public void disconnected(final SchedulerDriver schedulerDriver) {
        logger.warn("call disconnected");
        MesosStateService.deregister();
    }
    
    @Override
    public void slaveLost(final SchedulerDriver schedulerDriver, final Protos.SlaveID slaveID) {
        logger.warn("call slaveLost slaveID is: {}", slaveID);
        taskScheduler.expireAllLeasesByVMId(slaveID.getValue());
    }
    
    @Override
    public void executorLost(final SchedulerDriver schedulerDriver, final Protos.ExecutorID executorID, final Protos.SlaveID slaveID, final int i) {
        logger.warn("call executorLost slaveID is: {}, executorID is: {}", slaveID, executorID);
    }
    
    @Override
    public void error(final SchedulerDriver schedulerDriver, final String message) {
        logger.error("call error, message is: {}", message);
    }
}
