package com.ittovative.batchprocessing.util;

/**
 * The type Api response.
 *
 * @param <T> the type parameter
 */
public record APIResponse<T>(int statusCode, String message, T body) {
}