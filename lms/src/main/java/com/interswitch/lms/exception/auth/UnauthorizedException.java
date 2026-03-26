package com.interswitch.lms.exception.auth;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) { super(message); }
}