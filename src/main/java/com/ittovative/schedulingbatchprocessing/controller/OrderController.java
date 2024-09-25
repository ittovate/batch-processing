package com.ittovative.schedulingbatchprocessing.controller;

import com.ittovative.schedulingbatchprocessing.model.Order;
import com.ittovative.schedulingbatchprocessing.service.OrderService;
import com.ittovative.schedulingbatchprocessing.util.ApiResponse;
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
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/kafka")
    public ResponseEntity<ApiResponse<String>> makeOrderKafka(@RequestBody Order order) {
        this.orderService.sendOrderToKafka(order);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Order sent to kafka successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/db")
    public ResponseEntity<ApiResponse<String>> makeOrderDatabase(@RequestBody Order order) {
        this.orderService.sendOrderToDatabase(order);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Order sent to database successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/batch-db")
    public ResponseEntity<ApiResponse<String>> batchProcessDB() throws Exception {
        this.orderService.batchProcess(BatchReadType.DATABASE);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Batch processing from db started successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/batch-kafka")
    public ResponseEntity<ApiResponse<String>> batchProcessKafka() throws Exception {
        this.orderService.batchProcess(BatchReadType.KAFKA);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Batch processing from kafka started successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
