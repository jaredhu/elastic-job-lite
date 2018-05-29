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

package com.dangdang.ddframe.job.event.type;

import com.dangdang.ddframe.job.event.JobEvent;
import com.dangdang.ddframe.job.exception.ExceptionUtil;
import com.dangdang.ddframe.job.util.env.IpUtils;

import java.util.Date;
import java.util.UUID;

/**
 * 作业执行事件.
 *
 * @author zhangliang
 */
public final class JobExecutionEvent implements JobEvent {
    
    private String id = UUID.randomUUID().toString();
    
    private String hostname = IpUtils.getHostName();
    
    private String ip = IpUtils.getIp();
    
    private final String taskId;
    
    private final String jobName;
    
    private final ExecutionSource source;
    
    private final int shardingItem;
    
    private Date startTime = new Date();
    
    private Date completeTime;
    
    private boolean success;
    
    private JobExecutionEventThrowable failureCause;
    
    /**
     * 作业执行成功.
     * 
     * @return 作业执行事件
     */
    public JobExecutionEvent executionSuccess() {
        JobExecutionEvent result = new JobExecutionEvent(id, hostname, ip, taskId, jobName, source, shardingItem, startTime, completeTime, success, failureCause);
        result.setCompleteTime(new Date());
        result.setSuccess(true);
        return result;
    }
    
    /**
     * 作业执行失败.
     * 
     * @param failureCause 失败原因
     * @return 作业执行事件
     */
    public JobExecutionEvent executionFailure(final Throwable failureCause) {
        JobExecutionEvent result = new JobExecutionEvent(id, hostname, ip, taskId, jobName, source, shardingItem, startTime, completeTime, success, new JobExecutionEventThrowable(failureCause));
        result.setCompleteTime(new Date());
        result.setSuccess(false);
        return result;
    }

    public JobExecutionEvent(String taskId, String jobName, ExecutionSource source, int shardingItem) {
        this.taskId = taskId;
        this.jobName = jobName;
        this.source = source;
        this.shardingItem = shardingItem;
    }

    public JobExecutionEvent(String id, String hostname, String ip, String taskId, String jobName, ExecutionSource source, int shardingItem, Date startTime, Date completeTime, boolean success, JobExecutionEventThrowable failureCause) {
        this.id = id;
        this.hostname = hostname;
        this.ip = ip;
        this.taskId = taskId;
        this.jobName = jobName;
        this.source = source;
        this.shardingItem = shardingItem;
        this.startTime = startTime;
        this.completeTime = completeTime;
        this.success = success;
        this.failureCause = failureCause;
    }

    public String getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public String getTaskId() {
        return taskId;
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    public ExecutionSource getSource() {
        return source;
    }

    public int getShardingItem() {
        return shardingItem;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFailureCause(JobExecutionEventThrowable failureCause) {
        this.failureCause = failureCause;
    }

    /**
     * 获取失败原因.
     * 
     * @return 失败原因
     */
    public String getFailureCause() {
        return ExceptionUtil.transform(failureCause == null ? null : failureCause.getThrowable());
    }
    
    /**
     * 执行来源.
     */
    public enum ExecutionSource {
        NORMAL_TRIGGER, MISFIRE, FAILOVER
    }
}
