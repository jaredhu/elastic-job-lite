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

package com.dangdang.ddframe.job.cloud.scheduler.config.app;

/**
 * 云作业App配置对象.
 *
 * @author caohao
 */
public final class CloudAppConfiguration {
    
    private final String appName;
    
    private final String appURL;
    
    private final String bootstrapScript;
    
    private double cpuCount = 1;
    
    private double memoryMB = 128;
    
    private boolean appCacheEnable = true;
    
    private int eventTraceSamplingCount;

    public CloudAppConfiguration(String appName, String appURL, String bootstrapScript) {
        this.appName = appName;
        this.appURL = appURL;
        this.bootstrapScript = bootstrapScript;
    }

    public CloudAppConfiguration(String appName, String appURL, String bootstrapScript, double cpuCount, double memoryMB, boolean appCacheEnable, int eventTraceSamplingCount) {
        this.appName = appName;
        this.appURL = appURL;
        this.bootstrapScript = bootstrapScript;
        this.cpuCount = cpuCount;
        this.memoryMB = memoryMB;
        this.appCacheEnable = appCacheEnable;
        this.eventTraceSamplingCount = eventTraceSamplingCount;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppURL() {
        return appURL;
    }

    public String getBootstrapScript() {
        return bootstrapScript;
    }

    public double getCpuCount() {
        return cpuCount;
    }

    public double getMemoryMB() {
        return memoryMB;
    }

    public boolean isAppCacheEnable() {
        return appCacheEnable;
    }

    public int getEventTraceSamplingCount() {
        return eventTraceSamplingCount;
    }

    @Override
    public String toString() {
        return "CloudAppConfiguration{" +
                "appName='" + appName + '\'' +
                ", appURL='" + appURL + '\'' +
                ", bootstrapScript='" + bootstrapScript + '\'' +
                ", cpuCount=" + cpuCount +
                ", memoryMB=" + memoryMB +
                ", appCacheEnable=" + appCacheEnable +
                ", eventTraceSamplingCount=" + eventTraceSamplingCount +
                '}';
    }
}
