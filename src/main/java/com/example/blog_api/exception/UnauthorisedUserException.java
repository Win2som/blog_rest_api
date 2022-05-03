package com.example.blog_api.exception;

public class UnauthorisedUserException extends RuntimeException{
    public UnauthorisedUserException(String message){
        super(message);
    }
}
