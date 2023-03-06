package com.saru.commentservice.exceptionHandler;

import com.saru.commentservice.exception.CommentServiceException;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommentServiceExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentServiceException.class)
    public Map<String,String> handler(CommentServiceException exception){
        Map<String,String> ex=new HashMap<>();
        ex.put("errorMessage:", exception.getMessage());
        return ex;
    }
}
