package com.ittovative.batchprocessing.controller;

import static com.ittovative.batchprocessing.config.APIResponseConstant.ADD_ORDER_SUCCESSFUL_MESSAGE;
import static com.ittovative.batchprocessing.config.APIResponseConstant.BATCH_SUCCESS_MESSAGE;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.ADD_ORDER_DB_SUMMARY;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.ADD_ORDER_DESCRIPTION;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.ADD_ORDER_OK_RESPONSE_EXAMPLE;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.ADD_ORDER_REQUEST_EXAMPLE;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.ADD_ORDER_KAFKA_SUMMARY;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.START_BATCH_DESCRIPTION;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.START_BATCH_OK;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.START_BATCH_OK_RESPONSE_EXAMPLE;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.START_BATCH_SUMMARY_DB;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.START_BATCH_SUMMARY_KAFKA;
import static org.springframework.http.HttpStatus.CREATED;

import com.ittovative.batchprocessing.model.Order;
import com.ittovative.batchprocessing.service.OrderService;
import com.ittovative.batchprocessing.util.APIResponse;
import com.ittovative.batchprocessing.enums.BatchReadType;
import com.ittovative.batchprocessing.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
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
    @Operation(summary = ADD_ORDER_KAFKA_SUMMARY, description = ADD_ORDER_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = Order.class),
                            examples = @ExampleObject(ADD_ORDER_REQUEST_EXAMPLE))),
            responses = @ApiResponse(
            responseCode = "201",
            content = @Content(
                    schema = @Schema(implementation = APIResponse.class),
                    examples = @ExampleObject(ADD_ORDER_OK_RESPONSE_EXAMPLE)
            )
    ))
    @PostMapping("/kafka")
    public APIResponse<String> makeOrderKafka(@RequestBody Order orderDto) {
        orderService.sendOrderToKafka(orderDto);
        return ResponseUtil.createUnifiedResponse(CREATED.value(),
                ADD_ORDER_SUCCESSFUL_MESSAGE,null);
    }

    /**
     * Make order database response entity.
     *
     * @param order the order
     * @return the response entity
     */
    @Operation(summary = ADD_ORDER_DB_SUMMARY, description = ADD_ORDER_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = Order.class),
                            examples = @ExampleObject(ADD_ORDER_REQUEST_EXAMPLE))), responses = @ApiResponse(
            responseCode = "201",
            content = @Content(
                    schema = @Schema(implementation = APIResponse.class),
                    examples = @ExampleObject(ADD_ORDER_OK_RESPONSE_EXAMPLE)
            )
    ))
    @PostMapping("/db")
    public APIResponse<String> makeOrderDatabase(@RequestBody Order order) {
        orderService.sendOrderToDatabase(order);
        return ResponseUtil.createUnifiedResponse(CREATED.value(),ADD_ORDER_SUCCESSFUL_MESSAGE
                ,null);
    }

    /**
     * Batch process db response entity.
     *
     * @return the response entity
     * @throws Exception the exception
     */
    @Operation(summary = START_BATCH_SUMMARY_DB + " from database", description = START_BATCH_DESCRIPTION,
            responses = @ApiResponse(
            responseCode = START_BATCH_OK,
            content = @Content(
                    schema = @Schema(implementation = APIResponse.class),
                    examples = @ExampleObject(START_BATCH_OK_RESPONSE_EXAMPLE)
            )
    ))
    @PostMapping("/batch-db")
    public APIResponse<String> batchProcessDB() throws Exception {
        orderService.batchProcess(BatchReadType.DATABASE);
        return ResponseUtil.createUnifiedResponse(HttpStatus.OK.value(),
                BATCH_SUCCESS_MESSAGE,null);
    }

    /**
     * Batch process kafka response entity.
     *
     * @return the response entity
     * @throws Exception the exception
     */
    @Operation(summary = START_BATCH_SUMMARY_KAFKA, description = START_BATCH_DESCRIPTION,
            responses = @ApiResponse(
            responseCode = START_BATCH_OK,
            content = @Content(
                    schema = @Schema(implementation = APIResponse.class),
                    examples = @ExampleObject(START_BATCH_OK_RESPONSE_EXAMPLE)
            )
    ))
    @PostMapping("/batch-kafka")
    public APIResponse<String> batchProcessKafka() throws Exception {
        orderService.batchProcess(BatchReadType.KAFKA);
        return ResponseUtil.createUnifiedResponse(HttpStatus.OK.value(),
                BATCH_SUCCESS_MESSAGE,null);
    }

}
