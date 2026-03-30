package com.interswitch.lms.dto.response;   // Recommended package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;

    // ==================== Factory Methods ====================

    /**
     * Success response with message and data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Success response with data only (uses default message)
     */
    public static <T> ApiResponse<T> success(T data) {
        return success("Operation completed successfully", data);
    }

    /**
     * Error response
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Simple error response with message only
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(message, null);
    }
}