package com.ittovative.batchprocessing.util;

public class ApiResponse<T> {
    private final int statusCode;
    private final String message;
    private final T body;

    public ApiResponse(int statusCode, String message, T body) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }
}