package com.dragonfly.exception;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
