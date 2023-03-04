package com.saru.userservice.exception;

import org.springframework.stereotype.Component;


public class UserServiceNotFoundException extends Exception{
    public UserServiceNotFoundException(String message) {
        super(message);
    }
}
