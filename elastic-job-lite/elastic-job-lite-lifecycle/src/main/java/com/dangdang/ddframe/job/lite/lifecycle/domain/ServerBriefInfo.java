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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器维度简明信息对象.
 *
 * @author caohao
 */
public final class ServerBriefInfo implements Serializable, Comparable<ServerBriefInfo> {
    
    private static final long serialVersionUID = 1133149706443681483L;
    
    private final String serverIp;
    
    private final Set<String> instances = new HashSet<>();
    
    private final Set<String> jobNames = new HashSet<>();
    
    private int instancesNum;
    
    private int jobsNum;
    
    private AtomicInteger disabledJobsNum = new AtomicInteger();

    public ServerBriefInfo(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public Set<String> getInstances() {
        return instances;
    }

    public Set<String> getJobNames() {
        return jobNames;
    }

    public int getInstancesNum() {
        return instancesNum;
    }

    public void setInstancesNum(int instancesNum) {
        this.instancesNum = instancesNum;
    }

    public int getJobsNum() {
        return jobsNum;
    }

    public void setJobsNum(int jobsNum) {
        this.jobsNum = jobsNum;
    }

    public AtomicInteger getDisabledJobsNum() {
        return disabledJobsNum;
    }

    public void setDisabledJobsNum(AtomicInteger disabledJobsNum) {
        this.disabledJobsNum = disabledJobsNum;
    }

    @Override
    public int compareTo(final ServerBriefInfo o) {
        return (getServerIp()).compareTo(o.getServerIp());
    }
}
