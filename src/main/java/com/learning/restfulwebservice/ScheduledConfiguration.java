package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.IntervalType;
import com.learning.restfulwebservice.entity.WebSiteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Configuration
@EnableScheduling
@Component
public class ScheduledConfiguration implements SchedulingConfigurer {

    private TaskScheduler taskScheduler;
    @Autowired
    private SiteProcessor siteProcessor;
    @Autowired
    private WebSiteEntityDao webSiteEntityDao;

    private Map<String, ScheduledFuture<?>> siteScheduledTasks = new HashMap<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);// Set the pool of threads
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        this.taskScheduler = threadPoolTaskScheduler;// this will be used in later part of the article during refreshing the cron expression dynamically
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

    }

    public void removeJob(String siteName){
        siteScheduledTasks.computeIfPresent(siteName, (k,v)->{ v.cancel(false); return null;});
        siteScheduledTasks.remove(siteName);
    }

    public void job(WebSiteEntity webSiteEntity) {
        siteScheduledTasks.computeIfPresent(webSiteEntity.getName(), (k,v)->{ v.cancel(false); return null;});

        Duration duration = getDuration(webSiteEntity.getIntervalTime(), webSiteEntity.getIntervalType());
        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            @Transactional
            public void run() {
                siteProcessor.process(webSiteEntity);
                webSiteEntityDao.saveAndFlush(webSiteEntity);
                System.out.println(webSiteEntity.getName() +" status " + webSiteEntity.getWebSiteStatusEntity().getStatus()+"  at " + new Date());
            }
        }, Instant.now().plusSeconds(duration.getSeconds()), duration);

        siteScheduledTasks.put(webSiteEntity.getName(), scheduledFuture);
    }

    private static Duration getDuration(int interval, IntervalType intervalType) {
        switch (intervalType) {
            case SECONDS:
                return Duration.ofSeconds(interval);
            case MINUTES:
                return Duration.ofMinutes(interval);
            case HOURS:
                return Duration.ofHours(interval);
        }
        return Duration.ofHours(10000);
    }
}
