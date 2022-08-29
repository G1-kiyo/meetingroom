package com.format_project.meeting.model.dto;

import org.springframework.stereotype.Component;

@Component
public class AuthInfo {
    private String username;
    private String password;
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    


    /* public void setAccount(String account){
        this.account = account;
    }
    public String getAccount(){
        return account;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
 */
}
