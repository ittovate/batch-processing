package com.ittovative.batchprocessing.constant;

public class SwaggerConstant {
    public static final String TITLE = "Demo Batch Processing";
    public static final String VERSION = "0.0.1";
    public static final String DESCRIPTION = "Demo of using spring batch";
    public static final String CONTACT_NAME = "Mohanad Khaled";
    public static final String CONTACT_EMAIL = "mohanadkhaled87@gmail.com";
    public static final String CONTACT_URL = "https://github.com/orgs/ittovate/";
    public static final String DEVELOPMENT_SERVER_URL = "http://localhost:8080";
    public static final String DEVELOPMENT_SERVER_DESCRIPTION = "Development Server";

    //================================================= Add Order =================================================//

    public static final String ADD_ORDER_KAFKA_SUMMARY = "Add new order to kafka";
    public static final String ADD_ORDER_DB_SUMMARY = "Add new order to db";

    public static final String ADD_ORDER_DESCRIPTION = "Add a new order with the " +
            "specified name and description to the application for processing.";
    public static final String ADD_ORDER_CREATED = "201";
    public static final String ADD_ORDER_OK_RESPONSE_DESCRIPTION = "When order is sent successfully";
    public static final String ADD_ORDER_REQUEST_EXAMPLE = """
            {
              "name": "string",
              "description": "string"
            }
            """;
    public static final String ADD_ORDER_OK_RESPONSE_EXAMPLE = """
            {
              "statusCode": 201,
              "message": "Order sent successfully!",
              "body": null
            }
                        
            """;
    //================================================= Start Batch =================================================//
    private static final String START_BATCH_SUMMARY = "Start batch processing";

    public static final String START_BATCH_SUMMARY_KAFKA = START_BATCH_SUMMARY + " from kafka";
    public static final String START_BATCH_SUMMARY_DB = START_BATCH_SUMMARY + " from db";

    public static final String START_BATCH_DESCRIPTION = "Starts batch processing from the beginning unless there was a " +
            "previous failuer that was handled manually in spring batch's database , in this case it will restart after the " +
            "last successful commit successful.";
    public static final String START_BATCH_OK = "200";
    public static final String START_BATCH_SUCCESFUL_DESCRIPTION = "When batch is processed successfully";

    public static final String START_BATCH_OK_RESPONSE_EXAMPLE = """
            {
              "statusCode": 200,
              "message": "Batch ended successfully!!",
              "body": null
            }
                        
            """;
}
