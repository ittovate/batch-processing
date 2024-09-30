package com.ittovative.batchprocessing.service;

import static com.ittovative.batchprocessing.constant.AppConstant.KAFKA_TOPIC;

import com.ittovative.batchprocessing.model.Order;
import com.ittovative.batchprocessing.enums.BatchReadType;
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

    /**
     * Instantiates a new Order service.
     *
     * @param kafkaTemplate      the kafka template
     * @param applicationContext the application context
     * @param jobLauncher        the job launcher
     * @param jdbcTemplate       the jdbc template
     */
    public OrderService(KafkaTemplate<Long, Order> kafkaTemplate,
                        ApplicationContext applicationContext,
                        JobLauncher jobLauncher,
                        JdbcTemplate jdbcTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationContext = applicationContext;
        this.jobLauncher = jobLauncher;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Send order to kafka.
     *
     * @param order the order
     */
    public void sendOrderToKafka(Order order) {
        logger.info("Sending order: " + order);
        kafkaTemplate.send(KAFKA_TOPIC, order);
        logger.info("Order sent to kafka");
    }

    /**
     * Send order to database.
     *
     * @param order the order
     */
    public void sendOrderToDatabase(Order order) {
        logger.info("Sending order: " + order);
        String sql = "INSERT INTO orders (name, description) VALUES (?, ?)";
        jdbcTemplate.update(sql, order.name(), order.description());
        logger.info("Order saved into database");
    }

    /**
     * Batch process.
     *
     * @param batchReadType the batch read type
     * @throws Exception the exception
     */
    public void batchProcess(BatchReadType batchReadType) throws Exception {
        String beanName;
        if (batchReadType == BatchReadType.DATABASE) {
            beanName = "databaseOrderProcessingJob";
        } else if (batchReadType == BatchReadType.KAFKA) {
            beanName = "kafkaOrderProcessingJob";
        } else {
            throw new Exception("Read type is not supported!");
        }
        Job scheduledJob = (Job) applicationContext.getBean(beanName);
        Date date = new Date();
        logger.info("Job scheduled at: " + date);
        JobExecution jobExecution = jobLauncher.run(scheduledJob, new JobParameters());
        logger.info("Job execution completed with status: " + jobExecution.getStatus());
    }

}
