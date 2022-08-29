package com.format_project.meeting.exception;

public class JwtNotFoundException extends RuntimeException {
    
    public JwtNotFoundException(String msg){
        super(msg);
    }
}
