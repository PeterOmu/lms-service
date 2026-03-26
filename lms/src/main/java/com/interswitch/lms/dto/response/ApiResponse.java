package com.interswitch.lms.dto;

public class ApiResponse<T> {
    private String status; // "success" or "error"
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Factory methods for convenience
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("error", message, data);
    }

    // Getters & setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}