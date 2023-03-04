package com.saru.userservice.advice;

import com.saru.userservice.exception.UserServiceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserServiceExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserServiceNotFoundException.class)
    public Map<String,String> handleException(Exception exception){
        Map<String,String> ex=new HashMap<>();
        ex.put("errorMessage", exception.getMessage());
        return ex;
    }

}
