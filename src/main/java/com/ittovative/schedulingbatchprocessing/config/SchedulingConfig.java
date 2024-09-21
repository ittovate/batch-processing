package com.ittovative.schedulingbatchprocessing.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.logging.Logger;

//@EnableScheduling
@Configuration
public class SchedulingConfig {
    private final Logger logger = Logger.getLogger(SchedulingConfig.class.getName());
    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    public SchedulingConfig(JobLauncher jobLauncher,ApplicationContext applicationContext) {
        this.jobLauncher = jobLauncher;
        this.applicationContext = applicationContext;
    }

//    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    public void scheduledTask()
            throws JobInstanceAlreadyCompleteException,
                   JobExecutionAlreadyRunningException,
                   JobParametersInvalidException,
                   JobRestartException {
        Date date = new Date();
        logger.info("Job scheduled at: " + date);
        Job scheduledJob = (Job) applicationContext.getBean("orderProcessingJob");
        JobExecution jobExecution = jobLauncher.run(scheduledJob,new JobParameters());
        logger.info("Job execution completed with status: " + jobExecution.getStatus());
    }
}
