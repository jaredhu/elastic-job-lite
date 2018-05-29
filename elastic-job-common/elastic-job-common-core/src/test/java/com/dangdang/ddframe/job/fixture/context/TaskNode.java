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

package com.dangdang.ddframe.job.fixture.context;

import com.dangdang.ddframe.job.context.ExecutionType;
import com.google.common.base.Joiner;

//@Builder
public final class TaskNode {
    
    private String jobName;
    
    private int shardingItem;
    
    private ExecutionType type;
    
    private String slaveId;
    
    private String uuid;
    
    public String getTaskNodePath() {
        return Joiner.on("@-@").join(null == jobName ? "test_job" : jobName, shardingItem);
    }
    
    public String getTaskNodeValue() {
        return Joiner.on("@-@").join(getTaskNodePath(), null == type ? ExecutionType.READY : type, null == slaveId ? "slave-S0" : slaveId, null == uuid ? "0" : uuid);
    }

    public TaskNode(String jobName, int shardingItem, ExecutionType type, String slaveId, String uuid) {
        this.jobName = jobName;
        this.shardingItem = shardingItem;
        this.type = type;
        this.slaveId = slaveId;
        this.uuid = uuid;
    }

    public static <T> TaskNodeBuilder<T> builder() {
        return new TaskNodeBuilder<T>();
    }

    public static class TaskNodeBuilder<T> {
        private String jobName;

        private int shardingItem;

        private ExecutionType type;

        private String slaveId;

        private String uuid;

        private TaskNodeBuilder() {}

        public TaskNodeBuilder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public TaskNodeBuilder shardingItem(int shardingItem) {
            this.shardingItem = shardingItem;
            return this;
        }

        public TaskNodeBuilder type(ExecutionType type) {
            this.type = type;
            return this;
        }

        public TaskNodeBuilder slaveId(String slaveId) {
            this.slaveId = slaveId;
            return this;
        }

        public TaskNodeBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        @Override
        public String toString() {
            return "TaskNodeBuilder{" +
                    "jobName='" + jobName + '\'' +
                    ", shardingItem=" + shardingItem +
                    ", type=" + type +
                    ", slaveId='" + slaveId + '\'' +
                    ", uuid='" + uuid + '\'' +
                    '}';
        }

        public TaskNode build() {
            return new TaskNode(jobName, shardingItem, type, slaveId, uuid);
        }
    }
}
