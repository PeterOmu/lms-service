package com.interswitch.lms.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: Constructor for common use cases
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}