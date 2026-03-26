package com.interswitch.lms.exception;

import com.interswitch.lms.dto.ApiResponse;
import com.interswitch.lms.exception.auth.ForbiddenException;
import com.interswitch.lms.exception.auth.TokenExpiredException;
import com.interswitch.lms.exception.auth.UnauthorizedException;
import com.interswitch.lms.exception.course.AssignmentNotFoundException;
import com.interswitch.lms.exception.course.CourseNotFoundException;
import com.interswitch.lms.exception.course.DuplicateCourseException;
import com.interswitch.lms.exception.course.ModuleNotFoundException;
import com.interswitch.lms.exception.enrollment.AlreadyEnrolledException;
import com.interswitch.lms.exception.enrollment.NotEnrolledException;
import com.interswitch.lms.exception.enrollment.SubmissionNotAllowedException;
import com.interswitch.lms.exception.enrollment.SubmissionNotFoundException;
import com.interswitch.lms.exception.system.*;
import com.interswitch.lms.exception.user.InvalidCredentialsException;
import com.interswitch.lms.exception.user.RoleNotAllowedException;
import com.interswitch.lms.exception.user.UserAlreadyExistsException;
import com.interswitch.lms.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ===================== Helper =====================
    private <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        return new ResponseEntity<>(ApiResponse.error(message, data), status);
    }

    // ===================== Auth Exceptions =====================
    @ExceptionHandler({ UnauthorizedException.class, TokenExpiredException.class })
    public ResponseEntity<ApiResponse<String>> handleUnauthorized(RuntimeException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<String>> handleForbidden(ForbiddenException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), null);
    }

    // ===================== User Exceptions =====================
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserExists(UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler({ RoleNotAllowedException.class, InvalidCredentialsException.class })
    public ResponseEntity<ApiResponse<String>> handleForbiddenOrInvalid(RuntimeException ex) {
        HttpStatus status = ex instanceof RoleNotAllowedException ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        return buildResponse(status, ex.getMessage(), null);
    }

    // ===================== Course Exceptions =====================
    @ExceptionHandler({ CourseNotFoundException.class, ModuleNotFoundException.class, AssignmentNotFoundException.class })
    public ResponseEntity<ApiResponse<String>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(DuplicateCourseException.class)
    public ResponseEntity<ApiResponse<String>> handleConflict(DuplicateCourseException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // ===================== Enrollment Exceptions =====================
    @ExceptionHandler({ AlreadyEnrolledException.class, NotEnrolledException.class })
    public ResponseEntity<ApiResponse<String>> handleEnrollmentBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler({ SubmissionNotAllowedException.class })
    public ResponseEntity<ApiResponse<String>> handleSubmissionForbidden(SubmissionNotAllowedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), null);
    }

    @ExceptionHandler(SubmissionNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleSubmissionNotFound(SubmissionNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // ===================== System Exceptions =====================
    @ExceptionHandler({ InvalidInputException.class, DeadlineExceededException.class })
    public ResponseEntity<ApiResponse<String>> handleBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler({ ResourceConflictException.class, FileUploadException.class })
    public ResponseEntity<ApiResponse<String>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler({ InternalServerErrorException.class, DatabaseException.class })
    public ResponseEntity<ApiResponse<String>> handleInternalServer(RuntimeException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiResponse<String>> handleServiceUnavailable(ServiceUnavailableException ex) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), null);
    }

    // ===================== Fallback Exception =====================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage(), null);
    }
}
