package com.jfudali.coursesapp.exceptions;

public class AlreadyExistsException extends  RuntimeException{
    public AlreadyExistsException(String message){
        super(message);
    }
}
