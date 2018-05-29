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

package com.dangdang.ddframe.job.lite.lifecycle.domain;

import com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作业设置对象.
 * 
 * @author zhangliang
 */
public final class JobSettings implements Serializable {
    
    private static final long serialVersionUID = -6532210090618686688L;
    
    private String jobName;
    
    private String jobType;
    
    private String jobClass;
    
    private String cron;
    
    private int shardingTotalCount;
    
    private String shardingItemParameters;
    
    private String jobParameter;
    
    private boolean monitorExecution;
    
    private boolean streamingProcess;
    
    private int maxTimeDiffSeconds;
    
    private int monitorPort = -1;
    
    private boolean failover;
    
    private boolean misfire;
    
    private String jobShardingStrategyClass;
    
    private String description;
    
    private Map<String, String> jobProperties = new LinkedHashMap<>(JobPropertiesEnum.values().length, 1);
    
    private String scriptCommandLine;
    
    private int reconcileIntervalMinutes;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public boolean isMonitorExecution() {
        return monitorExecution;
    }

    public void setMonitorExecution(boolean monitorExecution) {
        this.monitorExecution = monitorExecution;
    }

    public boolean isStreamingProcess() {
        return streamingProcess;
    }

    public void setStreamingProcess(boolean streamingProcess) {
        this.streamingProcess = streamingProcess;
    }

    public int getMaxTimeDiffSeconds() {
        return maxTimeDiffSeconds;
    }

    public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
        this.maxTimeDiffSeconds = maxTimeDiffSeconds;
    }

    public int getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(int monitorPort) {
        this.monitorPort = monitorPort;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isMisfire() {
        return misfire;
    }

    public void setMisfire(boolean misfire) {
        this.misfire = misfire;
    }

    public String getJobShardingStrategyClass() {
        return jobShardingStrategyClass;
    }

    public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
        this.jobShardingStrategyClass = jobShardingStrategyClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getJobProperties() {
        return jobProperties;
    }

    public void setJobProperties(Map<String, String> jobProperties) {
        this.jobProperties = jobProperties;
    }

    public String getScriptCommandLine() {
        return scriptCommandLine;
    }

    public void setScriptCommandLine(String scriptCommandLine) {
        this.scriptCommandLine = scriptCommandLine;
    }

    public int getReconcileIntervalMinutes() {
        return reconcileIntervalMinutes;
    }

    public void setReconcileIntervalMinutes(int reconcileIntervalMinutes) {
        this.reconcileIntervalMinutes = reconcileIntervalMinutes;
    }
}
