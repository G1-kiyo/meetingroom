package com.format_project.meeting.model.entity;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OfflineMeeting {
    private String meetingId;
    private String roomId;
    private String userId;
    private String username;
    private String roomname;
    private String title;
    // 与validated连用 字符串转date类型
    @DateTimeFormat(pattern="yyyy-MM-dd")
    // 与@requestbody连用 date类型转json字符串
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date meetingDate;
    // 与validated连用 字符串转date类型
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    // 与@requestbody连用 date类型转json字符串
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date startTime;
    // 与validated连用 字符串转date类型
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    // 与@requestbody连用 date类型转json字符串
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date endTime;
    // 与validated连用 字符串转date类型
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    // 与@requestbody连用 date类型转json字符串
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    private String description;
    // 这里的元素指的是参会人员的userId
    // [{}]
    // member:[
    //     {
    //         userId:xxx
    //     }
    // ]
    private ArrayList<User> member;
    private int memberCount;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public ArrayList<User> getMember() {
        return member;
    }

    public void setMember(ArrayList<User> member) {
        this.member = member;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
    
}
