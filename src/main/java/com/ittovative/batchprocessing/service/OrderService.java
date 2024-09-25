package com.ittovative.batchprocessing.service;

import com.ittovative.batchprocessing.model.Order;
import com.ittovative.batchprocessing.util.BatchReadType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class OrderService {
    private final KafkaTemplate<Long, Order> kafkaTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(OrderService.class.getName());
    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;

    public OrderService(KafkaTemplate<Long, Order> kafkaTemplate,
                        ApplicationContext applicationContext,
                        JobLauncher jobLauncher,
                        JdbcTemplate jdbcTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationContext = applicationContext;
        this.jobLauncher = jobLauncher;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void sendOrderToKafka(Order order) {
        logger.info("Sending order: " + order);
        kafkaTemplate.send("orders", order);
        logger.info("Order sent to kafka");
    }

    public void sendOrderToDatabase(Order order) {
        logger.info("Sending order: " + order);
        String sql = "INSERT INTO orders (name, description) VALUES (?, ?)";
        jdbcTemplate.update(sql, order.name(), order.description());
        logger.info("Order saved into database");
    }
    public void batchProcess(BatchReadType batchReadType) throws Exception {
        String beanName;
        if (batchReadType.equals(BatchReadType.DATABASE)) {
            beanName = "databaseOrderProcessingJob";
        }
        else if (batchReadType.equals(BatchReadType.KAFKA)) {
            beanName = "kafkaOrderProcessingJob";
        }
        else {
            throw new Exception("Read type is not supported!");
        }
        Job scheduledJob = (Job) applicationContext.getBean(beanName);
        Date date = new Date();
        logger.info("Job scheduled at: " + date);
        JobExecution jobExecution = jobLauncher.run(scheduledJob,new JobParameters());
        logger.info("Job execution completed with status: " + jobExecution.getStatus());
    }

}
