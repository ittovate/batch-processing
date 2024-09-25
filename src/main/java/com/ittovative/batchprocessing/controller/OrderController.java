package com.ittovative.batchprocessing.controller;

import com.ittovative.batchprocessing.model.Order;
import com.ittovative.batchprocessing.service.OrderService;
import com.ittovative.batchprocessing.util.ApiResponse;
import com.ittovative.batchprocessing.util.BatchReadType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
