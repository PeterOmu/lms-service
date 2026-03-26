package com.interswitch.lms.exception.user;

public class RoleNotAllowedException extends RuntimeException {
    public RoleNotAllowedException(String message) { super(message); }
}