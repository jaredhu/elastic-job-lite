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

package com.dangdang.ddframe.job.lite.lifecycle.api;

import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.ShardingOperateAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.reg.RegistryCenterFactory;
import com.dangdang.ddframe.job.lite.lifecycle.internal.settings.JobSettingsAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.statistics.JobStatisticsAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.statistics.ServerStatisticsAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.statistics.ShardingStatisticsAPIImpl;
import com.google.common.base.Optional;

/**
 * 作业API工厂.
 *
 * @author zhangliang
 */
public final class JobAPIFactory {

    private JobAPIFactory() {
    }

    /**
     * 创建作业配置API对象.
     *
     * @param connectString 注册中心连接字符串
     * @param namespace 注册中心命名空间
     * @param digest 注册中心凭证
     * @return 作业配置API对象
     */
    public static JobSettingsAPI createJobSettingsAPI(final String connectString, final String namespace, final Optional<String> digest) {
        return new JobSettingsAPIImpl(RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, digest));
    }
    
    /**
     * 创建操作作业API对象.
     *
     * @param connectString 注册中心连接字符串
     * @param namespace 注册中心命名空间
     * @param digest 注册中心凭证
     * @return 操作作业API对象
     */
    public static JobOperateAPI createJobOperateAPI(final String connectString, final String namespace, final Optional<String> digest) {
        return new JobOperateAPIImpl(RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, digest));
    }
    
    /**
     * 创建操作分片API对象.
     *
     * @param connectString 注册中心连接字符串
     * @param namespace 注册中心命名空间
     * @param digest 注册中心凭证
     * @return 操作分片API对象
     */
    public static ShardingOperateAPI createShardingOperateAPI(final String connectString, final String namespace, final Optional<String> digest) {
        return new ShardingOperateAPIImpl(RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, digest));
    }
    
    /**
     * 创建作业状态展示API对象.
     *
     * @param connectString 注册中心连接字符串
     * @param namespace 注册中心命名空间
     * @param digest 注册中心凭证
     * @return 作业状态展示API对象
     */
    public static JobStatisticsAPI createJobStatisticsAPI(final String connectString, final String namespace, final Optional<String> digest) {
        return new JobStatisticsAPIImpl(RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, digest));
    }
    
    /**
     * 创建作业服务器状态展示API对象.
     *
     * @param connectString 注册中心连接字符串
     * @param namespace 注册中心命名空间
     * @param digest 注册中心凭证
     * @return 作业服务器状态展示API对象
     */
    public static ServerStatisticsAPI createServerStatisticsAPI(final String connectString, final String namespace, final Optional<String> digest) {
        return new ServerStatisticsAPIImpl(RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, digest));
    }
    
    /**
     * 创建作业分片状态展示API对象.
     *
     * @param connectString 注册中心连接字符串
     * @param namespace 注册中心命名空间
     * @param digest 注册中心凭证
     * @return 分片状态展示API对象
     */
    public static ShardingStatisticsAPI createShardingStatisticsAPI(final String connectString, final String namespace, final Optional<String> digest) {
        return new ShardingStatisticsAPIImpl(RegistryCenterFactory.createCoordinatorRegistryCenter(connectString, namespace, digest));
    }
}
