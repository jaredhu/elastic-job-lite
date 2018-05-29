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

package com.dangdang.ddframe.job.config.dataflow;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;

/**
 * 数据流作业配置信息.
 * 
 * @author caohao
 * @author zhangliang
 */
public final class DataflowJobConfiguration implements JobTypeConfiguration {
    
    private final JobCoreConfiguration coreConfig;
    
    private final JobType jobType = JobType.DATAFLOW;
    
    private final String jobClass;

    //流数据处理
    private final boolean streamingProcess;

    public DataflowJobConfiguration(JobCoreConfiguration coreConfig, String jobClass, boolean streamingProcess) {
        this.coreConfig = coreConfig;
        this.jobClass = jobClass;
        this.streamingProcess = streamingProcess;
    }

    @Override
    public JobCoreConfiguration getCoreConfig() {
        return coreConfig;
    }

    @Override
    public JobType getJobType() {
        return jobType;
    }

    @Override
    public String getJobClass() {
        return jobClass;
    }

    /**
     * 是否流数据处理
     * @return true or false
     */
    public boolean isStreamingProcess() {
        return streamingProcess;
    }
}
