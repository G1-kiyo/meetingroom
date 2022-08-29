package com.format_project.meeting.exception;

public class JwtValidationFailException extends RuntimeException{
    public JwtValidationFailException(String msg){
        super(msg);
    }
    public JwtValidationFailException(){

    }
}
