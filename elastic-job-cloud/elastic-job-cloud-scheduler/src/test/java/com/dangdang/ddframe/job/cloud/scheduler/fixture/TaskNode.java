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

package com.dangdang.ddframe.job.cloud.scheduler.fixture;

import com.dangdang.ddframe.job.context.ExecutionType;
import com.dangdang.ddframe.job.context.TaskContext.MetaInfo;
import com.google.common.base.Joiner;

public final class TaskNode {

    private String jobName;

    private int shardingItem;

    private ExecutionType type;

    private String slaveId;

    private String uuid;

    public TaskNode(String jobName, int shardingItem, ExecutionType type, String slaveId, String uuid) {
        this.jobName = jobName;
        this.shardingItem = shardingItem;
        this.type = type;
        this.slaveId = slaveId;
        this.uuid = uuid;
    }

    public String getTaskNodePath() {
        return Joiner.on("@-@").join(null == jobName ? "test_job" : jobName, shardingItem);
    }

    public String getTaskNodeValue() {
        return Joiner.on("@-@").join(getTaskNodePath(), null == type ? ExecutionType.READY : type, null == slaveId ? "slave-S0" : slaveId, null == uuid ? "0" : uuid);
    }

    public MetaInfo getMetaInfo() {
        return MetaInfo.from(Joiner.on("@-@").join("test_job", 0));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String jobName;

        private int shardingItem;

        private ExecutionType type;

        private String slaveId;

        private String uuid;

        private Builder() {
        }

        public Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder shardingItem(int shardingItem) {
            this.shardingItem = shardingItem;
            return this;
        }


        public Builder type(ExecutionType type) {
            this.type = type;
            return this;
        }


        public Builder slaveId(String slaveId) {
            this.slaveId = slaveId;
            return this;
        }

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }


        public TaskNode build() {
            return new TaskNode(jobName, shardingItem, type, slaveId, uuid);
        }

    }
}
