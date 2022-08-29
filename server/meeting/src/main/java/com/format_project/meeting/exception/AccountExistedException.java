package com.format_project.meeting.exception;

public class AccountExistedException extends RuntimeException {
    public AccountExistedException(String msg){
        super(msg);
    }
    public AccountExistedException(){
        super();
    }
    
}
