package solvve.course.exception.handler;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private final HttpStatus status;
    private final Class exceptionClass;
    private String message;

    ErrorInfo(HttpStatus status, Class exceptionClass, String message) {
        this.status = status;
        this.exceptionClass = exceptionClass;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public Class getExceptionClass() { return exceptionClass; }
    public String getMessage() { return message; }
}
