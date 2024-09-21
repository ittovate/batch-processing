package com.ittovative.schedulingbatchprocessing.service;

import com.ittovative.schedulingbatchprocessing.model.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class OrderService {
    private final KafkaTemplate<Long, Order> kafkaTemplate;
    private final Logger logger = Logger.getLogger(OrderService.class.getName());
    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;

    public OrderService(KafkaTemplate<Long, Order> kafkaTemplate,
                        ApplicationContext applicationContext,
                        JobLauncher jobLauncher) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationContext = applicationContext;
        this.jobLauncher = jobLauncher;
    }

    public void makeOrder(Order order) {
        logger.info("Sending order: " + order);
        kafkaTemplate.send("orders", order);
        logger.info("Order sent to kafka");
    }
    public void batchProcess() throws JobInstanceAlreadyCompleteException,
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
