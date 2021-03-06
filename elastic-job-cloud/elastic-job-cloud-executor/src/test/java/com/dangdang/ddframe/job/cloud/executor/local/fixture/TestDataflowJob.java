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

package com.dangdang.ddframe.job.cloud.executor.local.fixture;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public final class TestDataflowJob implements DataflowJob<String> {
    
    private static List<String> input;
    
    private static List<String> output;

    public static void setInput(List<String> input) {
        TestDataflowJob.input = input;
    }

    public static List<String> getOutput() {
        return output;
    }

    public static void setOutput(List<String> output) {
        TestDataflowJob.output = output;
    }

    @Override
    public List<String> fetchData(final ShardingContext shardingContext) {
        return input;
    }
    
    @Override
    public void processData(final ShardingContext shardingContext, final List<String> data) {
        output = Lists.transform(input, new Function<String, String>() {
            @Override
            public String apply(final String input) {
                return input + "-d";
            }
        });
    }
}
