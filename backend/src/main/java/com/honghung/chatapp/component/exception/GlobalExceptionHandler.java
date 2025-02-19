package com.honghung.chatapp.component.exception; 

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.honghung.chatapp.dto.response.ErrorResponseEntity;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorResponseEntity handleArgumentNotValideException(MethodArgumentNotValidException exception, WebRequest request) {
        ErrorResponseEntity.Payload data = new ErrorResponseEntity.Payload();
        data.setTimestamp(new Date());
        data.setStatusCode(exception.getStatusCode().value());
        data.setMessage(exception.getAllErrors().get(0).getDefaultMessage());
        data.setPath(request.getDescription(false).replace("uri=", ""));
        data.setErrorCode("invalid_user_input");
        return new ErrorResponseEntity(data);
    }

    // for @PathVarialble, @RequestParam validation
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponseEntity handleHandlerMethodValidationException(HandlerMethodValidationException ex, WebRequest request) {
        ErrorResponseEntity.Payload payload = new ErrorResponseEntity.Payload();
        payload.setStatusCode(HttpStatus.BAD_REQUEST.value());
        payload.setPath(request.getDescription(false).replace("uri=", ""));
        payload.setMessage(ex.getMessage());
        payload.setErrorCode("validation_error");
        payload.setTimestamp(new Date());

        return new ErrorResponseEntity(payload);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ErrorResponseEntity handleBusinessLogicException(BusinessException exception, WebRequest request) {

        ErrorResponseEntity.Payload data = new ErrorResponseEntity.Payload();
        data.setTimestamp(new Date());

        data.setStatusCode(400);
        data.setMessage(exception.getMessage());
        data.setPath(request.getDescription(false).replace("uri=", ""));
        data.setErrorCode(exception.getCode());
        return new ErrorResponseEntity(data);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ErrorResponseEntity handleMethodUnsupported(HttpRequestMethodNotSupportedException exception, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponseEntity.Payload data = new ErrorResponseEntity.Payload();
        if(exception.getMethod().equalsIgnoreCase("GET") && path.equals("/auth/login")) {
            data.setStatusCode(401);
            data.setMessage("Unauthorized");
            data.setErrorCode("unauthorized");
        }
        else {
            data.setStatusCode(400);
            data.setMessage("Request method not supported");
            data.setErrorCode("method_not_supported");
        }
        data.setTimestamp(new Date());
        data.setPath(path);
        return new ErrorResponseEntity(data);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ErrorResponseEntity handleRuntimeException(Exception exception, WebRequest request) {
        ErrorResponseEntity.Payload data = new ErrorResponseEntity.Payload();
        log.error("Error: ", exception);
        data.setTimestamp(new Date());
        data.setStatusCode(500);
        data.setMessage(exception.getLocalizedMessage());
        data.setErrorCode("error_processing_request");
        data.setPath(request.getDescription(false).replace("uri=", ""));
        return new ErrorResponseEntity(data);
    }

    @ExceptionHandler(value = Exception.class)
    public ErrorResponseEntity handleException(Exception exception, WebRequest request) {
        log.error("Error: ", exception);
        ErrorResponseEntity.Payload data = new ErrorResponseEntity.Payload();
        data.setTimestamp(new Date());
        data.setStatusCode(500);
        data.setMessage("Unexpected error occurred in server");
        data.setErrorCode("something_went_wrong");
        data.setPath(request.getDescription(false).replace("uri=", ""));
        return new ErrorResponseEntity(data);
    }
}
