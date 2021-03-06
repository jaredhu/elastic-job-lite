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

package com.dangdang.ddframe.job.config.script;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.api.script.ScriptJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;

/**
 * 脚本作业配置.
 * 
 * @author caohao
 * @author zhangliang
 */
public final class ScriptJobConfiguration implements JobTypeConfiguration {
    
    private final JobCoreConfiguration coreConfig;
    
    private final JobType jobType = JobType.SCRIPT;
    
    private final String jobClass = ScriptJob.class.getCanonicalName();
    
    private final String scriptCommandLine;

    public ScriptJobConfiguration(JobCoreConfiguration coreConfig, String scriptCommandLine) {
        this.coreConfig = coreConfig;
        this.scriptCommandLine = scriptCommandLine;
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

    public String getScriptCommandLine() {
        return scriptCommandLine;
    }
}
