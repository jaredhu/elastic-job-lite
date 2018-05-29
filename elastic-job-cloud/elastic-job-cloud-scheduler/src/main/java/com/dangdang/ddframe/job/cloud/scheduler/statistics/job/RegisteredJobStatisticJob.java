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

package com.dangdang.ddframe.job.cloud.scheduler.statistics.job;

import com.dangdang.ddframe.job.cloud.scheduler.config.job.CloudJobConfigurationService;
import com.dangdang.ddframe.job.cloud.scheduler.statistics.util.StatisticTimeUtils;
import com.dangdang.ddframe.job.statistics.StatisticInterval;
import com.dangdang.ddframe.job.statistics.rdb.StatisticRdbRepository;
import com.dangdang.ddframe.job.statistics.type.job.JobRegisterStatistics;
import com.google.common.base.Optional;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已注册作业统计作业.
 *
 * @author liguangyun
 */
public final class RegisteredJobStatisticJob extends AbstractStatisticJob {

    private static final Logger logger = LoggerFactory.getLogger(RegisteredJobStatisticJob.class);
    
    private CloudJobConfigurationService configurationService;
    
    private StatisticRdbRepository repository;
    
    private final StatisticInterval execInterval = StatisticInterval.DAY;

    public RegisteredJobStatisticJob() {
    }

    public RegisteredJobStatisticJob(CloudJobConfigurationService configurationService, StatisticRdbRepository repository) {
        this.configurationService = configurationService;
        this.repository = repository;
    }

    public void setConfigurationService(CloudJobConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setRepository(StatisticRdbRepository repository) {
        this.repository = repository;
    }

    @Override
    public JobDetail buildJobDetail() {
        return JobBuilder.newJob(this.getClass()).withIdentity(getJobName()).build();
    }
    
    @Override
    public Trigger buildTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerName())
                .withSchedule(CronScheduleBuilder.cronSchedule(execInterval.getCron())
                .withMisfireHandlingInstructionDoNothing()).build();
    }
    
    @Override
    public Map<String, Object> getDataMap() {
        Map<String, Object> result = new HashMap<>(2);
        result.put("configurationService", configurationService);
        result.put("repository", repository);
        return result;
    }
    
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        Optional<JobRegisterStatistics> latestOne = repository.findLatestJobRegisterStatistics();
        if (latestOne.isPresent()) {
            fillBlankIfNeeded(latestOne.get());
        }
        int registeredCount = configurationService.loadAll().size();
        JobRegisterStatistics jobRegisterStatistics = new JobRegisterStatistics(registeredCount, StatisticTimeUtils.getCurrentStatisticTime(execInterval));
        logger.debug("Add jobRegisterStatistics, registeredCount is:{}", registeredCount);
        repository.add(jobRegisterStatistics);
    }
    
    private void fillBlankIfNeeded(final JobRegisterStatistics latestOne) {
        List<Date> blankDateRange = findBlankStatisticTimes(latestOne.getStatisticsTime(), execInterval);
        if (!blankDateRange.isEmpty()) {
            logger.debug("Fill blank range of jobRegisterStatistics, range is:{}", blankDateRange);
        }
        for (Date each : blankDateRange) {
            repository.add(new JobRegisterStatistics(latestOne.getRegisteredCount(), each));
        }
    }
}
