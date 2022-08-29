package com.format_project.meeting.model.entity;

public class MeetingRoom {
    
    private String roomId;
    private String roomname;
    private String roomImg;
    private String scale;
    private int status;
    private String description;
    private String contact;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getRoomImg() {
        return roomImg;
    }
    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getScale() {
        return scale;
    }
    public void setScale(String scale) {
        this.scale = scale;
    }
    
    public String getRoomname() {
        return roomname;
    }
    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    
}
