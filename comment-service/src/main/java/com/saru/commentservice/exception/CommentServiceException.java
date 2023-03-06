package com.saru.commentservice.exception;

import org.springframework.stereotype.Component;


public class CommentServiceException extends Exception{

    public CommentServiceException(String message) {
        super(message);
    }
}
