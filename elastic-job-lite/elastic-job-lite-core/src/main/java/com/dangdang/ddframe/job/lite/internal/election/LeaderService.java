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

package com.dangdang.ddframe.job.lite.internal.election;

import com.dangdang.ddframe.job.lite.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.lite.internal.server.ServerService;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.job.lite.internal.storage.LeaderExecutionCallback;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.util.concurrent.BlockUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主节点服务.
 * 
 * @author zhangliang
 */
public final class LeaderService {

    private static final Logger logger = LoggerFactory.getLogger(LeaderService.class);
    
    private final String jobName;
    
    private final ServerService serverService;
    
    private final JobNodeStorage jobNodeStorage;
    
    public LeaderService(final CoordinatorRegistryCenter regCenter, final String jobName) {
        this.jobName = jobName;
        jobNodeStorage = new JobNodeStorage(regCenter, jobName);
        serverService = new ServerService(regCenter, jobName);
    }
    
    /**
     * 选举主节点.
     */
    public void electLeader() {
        logger.debug("Elect a new leader now.");
        jobNodeStorage.executeInLeader(LeaderNode.LATCH, new LeaderElectionExecutionCallback());
        logger.debug("Leader election completed.");
    }
    
    /**
     * 判断当前节点是否是主节点.
     * 
     * <p>
     * 如果主节点正在选举中而导致取不到主节点, 则阻塞至主节点选举完成再返回.
     * </p>
     * 
     * @return 当前节点是否是主节点
     */
    public boolean isLeaderUntilBlock() {
        while (!hasLeader() && serverService.hasAvailableServers()) {
            logger.info("Leader is electing, waiting for {} ms", 100);
            BlockUtils.waitingShortTime();
            if (!JobRegistry.getInstance().isShutdown(jobName) && serverService.isAvailableServer(JobRegistry.getInstance().getJobInstance(jobName).getIp())) {
                electLeader();
            }
        }
        return isLeader();
    }
    
    /**
     * 判断当前节点是否是主节点.
     *
     * @return 当前节点是否是主节点
     */
    public boolean isLeader() {
        return !JobRegistry.getInstance().isShutdown(jobName) && JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId().equals(jobNodeStorage.getJobNodeData(LeaderNode.INSTANCE));
    }
    
    /**
     * 判断是否已经有主节点.
     * 
     * @return 是否已经有主节点
     */
    public boolean hasLeader() {
        return jobNodeStorage.isJobNodeExisted(LeaderNode.INSTANCE);
    }
    
    /**
     * 删除主节点供重新选举.
     */
    public void removeLeader() {
        jobNodeStorage.removeJobNodeIfExisted(LeaderNode.INSTANCE);
    }
    
    class LeaderElectionExecutionCallback implements LeaderExecutionCallback {

        public LeaderElectionExecutionCallback() {
        }

        @Override
        public void execute() {
            if (!hasLeader()) {
                jobNodeStorage.fillEphemeralJobNode(LeaderNode.INSTANCE, JobRegistry.getInstance().getJobInstance(jobName).getJobInstanceId());
            }
        }
    }
}
