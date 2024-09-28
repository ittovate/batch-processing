package com.ittovative.batchprocessing.controller;

import com.ittovative.batchprocessing.dto.OrderDto;
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

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService the order service
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Make order kafka response entity.
     *
     * @param order the order
     * @return the response entity
     */
    @PostMapping("/kafka")
    public ResponseEntity<ApiResponse<String>> makeOrderKafka(@RequestBody Order order) {
        orderService.sendOrderToKafka(order);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Order sent to kafka successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Make order database response entity.
     *
     * @param order the order
     * @return the response entity
     */
    @PostMapping("/db")
    public ResponseEntity<ApiResponse<String>> makeOrderDatabase(@RequestBody OrderDto order) {
        Order convertedOrder = new Order(order.name(),order.description());
        orderService.sendOrderToDatabase(convertedOrder);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Order sent to database successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Batch process db response entity.
     *
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping("/batch-db")
    public ResponseEntity<ApiResponse<String>> batchProcessDB() throws Exception {
        orderService.batchProcess(BatchReadType.DATABASE);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Batch processing from db started successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Batch process kafka response entity.
     *
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping("/batch-kafka")
    public ResponseEntity<ApiResponse<String>> batchProcessKafka() throws Exception {
        orderService.batchProcess(BatchReadType.KAFKA);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Batch processing from kafka started successfully!",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
