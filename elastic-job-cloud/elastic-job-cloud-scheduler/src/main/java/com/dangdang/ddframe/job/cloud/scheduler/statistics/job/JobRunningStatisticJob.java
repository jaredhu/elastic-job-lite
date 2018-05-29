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

package com.dangdang.ddframe.job.cloud.scheduler.statistics.job;

import com.dangdang.ddframe.job.cloud.scheduler.state.running.RunningService;
import com.dangdang.ddframe.job.cloud.scheduler.statistics.util.StatisticTimeUtils;
import com.dangdang.ddframe.job.context.TaskContext;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.statistics.StatisticInterval;
import com.dangdang.ddframe.job.statistics.rdb.StatisticRdbRepository;
import com.dangdang.ddframe.job.statistics.type.job.JobRunningStatistics;
import com.dangdang.ddframe.job.statistics.type.task.TaskRunningStatistics;
import com.google.common.base.Optional;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 运行中的任务统计作业.
 *
 * @author liguangyun
 */
public final class JobRunningStatisticJob extends AbstractStatisticJob {

    private static final Logger logger = LoggerFactory.getLogger(JobRunningStatisticJob.class);
    
    private static final StatisticInterval EXECUTE_INTERVAL = StatisticInterval.MINUTE;
    
    private RunningService runningService;
    
    private StatisticRdbRepository repository;

    public JobRunningStatisticJob() {
    }

    /**
     * 构造函数.
     * @param registryCenter 注册中心
     * @param rdbRepository 基于rdb的数据仓库对象
     */
    public JobRunningStatisticJob(final CoordinatorRegistryCenter registryCenter, final StatisticRdbRepository rdbRepository) {
        runningService = new RunningService(registryCenter);
        this.repository = rdbRepository;
    }

    public void setRunningService(RunningService runningService) {
        this.runningService = runningService;
    }

    public void setRepository(StatisticRdbRepository repository) {
        this.repository = repository;
    }

    @Override
    public JobDetail buildJobDetail() {
        return JobBuilder.newJob(this.getClass()).withIdentity(getJobName()).build();
    }
    
    @Override
    public Trigger buildTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerName())
                .withSchedule(CronScheduleBuilder.cronSchedule(EXECUTE_INTERVAL.getCron())
                .withMisfireHandlingInstructionDoNothing()).build();
    }
    
    @Override
    public Map<String, Object> getDataMap() {
        Map<String, Object> result = new HashMap<>(2);
        result.put("runningService", runningService);
        result.put("repository", repository);
        return result;
    }
    
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        Map<String, Set<TaskContext>> allRunningTasks = runningService.getAllRunningTasks();
        statisticJob(getJobRunningCount(allRunningTasks));
        statisticTask(getTaskRunningCount(allRunningTasks));
    }
    
    private void statisticJob(final int runningCount) {
        Optional<JobRunningStatistics> latestOne = repository.findLatestJobRunningStatistics();
        if (latestOne.isPresent()) {
            fillBlankIfNeeded(latestOne.get());
        }
        JobRunningStatistics jobRunningStatistics = new JobRunningStatistics(runningCount, StatisticTimeUtils.getCurrentStatisticTime(EXECUTE_INTERVAL));
        logger.debug("Add jobRunningStatistics, runningCount is:{}", runningCount);
        repository.add(jobRunningStatistics);
    }
    
    private void statisticTask(final int runningCount) {
        Optional<TaskRunningStatistics> latestOne = repository.findLatestTaskRunningStatistics();
        if (latestOne.isPresent()) {
            fillBlankIfNeeded(latestOne.get());
        }
        TaskRunningStatistics taskRunningStatistics = new TaskRunningStatistics(runningCount, StatisticTimeUtils.getCurrentStatisticTime(EXECUTE_INTERVAL));
        logger.debug("Add taskRunningStatistics, runningCount is:{}", runningCount);
        repository.add(taskRunningStatistics);
    }
    
    private int getJobRunningCount(final Map<String, Set<TaskContext>> allRunningTasks) {
        int result = 0;
        for (Map.Entry<String, Set<TaskContext>> entry : allRunningTasks.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                result++;
            }
        }
        return result;
    }
    
    private int getTaskRunningCount(final Map<String, Set<TaskContext>> allRunningTasks) {
        int result = 0;
        for (Map.Entry<String, Set<TaskContext>> entry : allRunningTasks.entrySet()) {
            result += entry.getValue().size();
        }
        return result;
    }
    
    private void fillBlankIfNeeded(final JobRunningStatistics latestOne) {
        List<Date> blankDateRange = findBlankStatisticTimes(latestOne.getStatisticsTime(), EXECUTE_INTERVAL);
        if (!blankDateRange.isEmpty()) {
            logger.debug("Fill blank range of jobRunningStatistics, range is:{}", blankDateRange);
        }
        for (Date each : blankDateRange) {
            repository.add(new JobRunningStatistics(latestOne.getRunningCount(), each));
        }
    }
    
    private void fillBlankIfNeeded(final TaskRunningStatistics latestOne) {
        List<Date> blankDateRange = findBlankStatisticTimes(latestOne.getStatisticsTime(), EXECUTE_INTERVAL);
        if (!blankDateRange.isEmpty()) {
            logger.debug("Fill blank range of taskRunningStatistics, range is:{}", blankDateRange);
        }
        for (Date each : blankDateRange) {
            repository.add(new TaskRunningStatistics(latestOne.getRunningCount(), each));
        }
    }
}
