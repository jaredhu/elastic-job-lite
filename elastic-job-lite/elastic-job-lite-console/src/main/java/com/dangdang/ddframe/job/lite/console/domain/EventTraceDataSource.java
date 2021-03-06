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

package com.dangdang.ddframe.job.lite.console.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 事件追踪数据源.
 * 
 * @author zhangxinguo
 */
public final class EventTraceDataSource {

    private static final Logger logger = LoggerFactory.getLogger(EventTraceDataSource.class);
    
    private EventTraceDataSourceConfiguration eventTraceDataSourceConfiguration;

    protected EventTraceDataSourceConfiguration getEventTraceDataSourceConfiguration() {
        return eventTraceDataSourceConfiguration;
    }

    public EventTraceDataSource(final EventTraceDataSourceConfiguration eventTraceDataSourceConfiguration) {
        this.eventTraceDataSourceConfiguration = eventTraceDataSourceConfiguration;
    }
    
    /**
     * 初始化.
     */
    public void init() {
        logger.debug("Elastic job: data source init, connection url is: {}.", eventTraceDataSourceConfiguration.getUrl());
        try {
            Class.forName(eventTraceDataSourceConfiguration.getDriver());
            DriverManager.getConnection(eventTraceDataSourceConfiguration.getUrl(), eventTraceDataSourceConfiguration.getUsername(), eventTraceDataSourceConfiguration.getPassword());
        } catch (final ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
