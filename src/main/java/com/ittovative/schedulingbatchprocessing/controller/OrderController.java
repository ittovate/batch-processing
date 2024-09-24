package com.ittovative.schedulingbatchprocessing.controller;

import com.ittovative.schedulingbatchprocessing.model.Order;
import com.ittovative.schedulingbatchprocessing.service.OrderService;
import com.ittovative.schedulingbatchprocessing.util.BatchReadType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1")
public class OrderController {
    private final OrderService orderService;
    private final Logger logger = Logger.getLogger(OrderController.class.getName());
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/kafka")
    public ResponseEntity<String> makeOrderKafka(@RequestBody Order order) {
        logger.info("Making order: " + order);
        this.orderService.sendOrderToKafka(order);
        logger.info("Sending response after making order");
        return new ResponseEntity<>("Order successful!", HttpStatus.OK);
    }
    @PostMapping("/db")
    public ResponseEntity<String> makeOrderDatabase(@RequestBody Order order) {
        logger.info("Making order: " + order);
        this.orderService.sendOrderToDatabase(order);
        logger.info("Sending response after making order");
        return new ResponseEntity<>("Order successful!", HttpStatus.OK);
    }
    @PostMapping("/batch-db")
    public ResponseEntity<String> batchProcessDB() throws Exception {
        logger.info("Starting batch process");
        this.orderService.batchProcess(BatchReadType.DATABASE);
        logger.info("Ending batch process");
        return new ResponseEntity<>("Order successful!", HttpStatus.OK);
    }
    @PostMapping("/batch-kafka")
    public ResponseEntity<String> batchProcessKafka() throws Exception {
        logger.info("Starting batch process");
        this.orderService.batchProcess(BatchReadType.KAFKA);
        logger.info("Ending batch process");
        return new ResponseEntity<>("Order successful!", HttpStatus.OK);
    }

}
