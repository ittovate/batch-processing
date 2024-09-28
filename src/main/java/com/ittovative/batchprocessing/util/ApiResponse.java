package com.ittovative.batchprocessing.util;

/**
 * The type Api response.
 *
 * @param <T> the type parameter
 */
public class ApiResponse<T> {
    private final int statusCode;
    private final String message;
    private final T body;

    /**
     * Instantiates a new Api response.
     *
     * @param statusCode the status code
     * @param message    the message
     * @param body       the body
     */
    public ApiResponse(int statusCode, String message, T body) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }

    /**
     * Gets status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public T getBody() {
        return body;
    }
}
