package solvve.course.exception.handler;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import solvve.course.exception.UnprocessableEntityException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handlerException(Exception ex) {

        ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        HttpStatus httpStatus = status != null ? status.code() : HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorInfo errorInfo = new ErrorInfo(httpStatus, ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {

        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST, ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ UnprocessableEntityException.class })
    public ResponseEntity<Object> handleUnprocessableEntityException(UnprocessableEntityException ex) {

        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST, ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {

        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.FORBIDDEN, ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
}