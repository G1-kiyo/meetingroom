package com.format_project.meeting.model.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OnlineMeeting{
    private String meetingId;
    private String userId;
    private String meetingType;
    private String topic;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date scheduleMeetingDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date scheduleStartTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date scheduleEndTime;
    private Long scheduleTimespan;
    private boolean isInvited;

    // @JsonFormat(pattern="yyyy-MM-dd")
    // @DateTimeFormat(pattern="yyyy-MM-dd")
    // private Date actualMeetingDate;
    // @JsonFormat(pattern="HH:mm:ss")
    // @DateTimeFormat(pattern="HH:mm:ss")
    // private Date actualStartTime;
    // private Long actualTimespan;
    // @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    // @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // public Date getActualStartTime() {
    //     return actualStartTime;
    // }

    // public void setActualStartTime(Date actualStartTime) {
    //     this.actualStartTime = actualStartTime;
    // }


    // public Date getActualMeetingDate() {
    //     return actualMeetingDate;
    // }

    // public void setActualMeetingDate(Date actualMeetingDate) {
    //     this.actualMeetingDate = actualMeetingDate;
    // }

    
    // public Long getActualTimespan() {
    //     return actualTimespan;
    // }

    // public void setActualTimespan(Long actualTimespan) {
    //     this.actualTimespan = actualTimespan;
    // }

    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean isInvited) {
        this.isInvited = isInvited;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getScheduleTimespan() {
        return scheduleTimespan;
    }

    public void setScheduleTimespan(Long scheduleTimespan) {
        this.scheduleTimespan = scheduleTimespan;
    }

    public Date getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setgetScheduleStartTime(Date scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public Date getScheduleMeetingDate() {
        return scheduleMeetingDate;
    }

    public void setScheduleMeetingDate(Date scheduleMeetingDate) {
        this.scheduleMeetingDate = scheduleMeetingDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

}