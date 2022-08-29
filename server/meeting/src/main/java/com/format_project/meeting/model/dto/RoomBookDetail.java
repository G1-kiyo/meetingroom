package com.format_project.meeting.model.dto;

import com.format_project.meeting.model.entity.MeetingRoom;
import com.format_project.meeting.model.entity.OfflineMeeting;
import com.format_project.meeting.model.entity.User;

public class RoomBookDetail extends OfflineMeeting {
    
    private MeetingRoom meetingRoom;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }
}
