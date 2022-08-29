package com.format_project.meeting.model.dto;

public class PwdReset {
    
    private String uid;
    private String userId;
    private boolean isExpired;
    public boolean isExpired() {
        return isExpired;
    }
    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    
}
