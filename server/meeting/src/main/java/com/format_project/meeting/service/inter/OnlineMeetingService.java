package com.format_project.meeting.service.inter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.format_project.meeting.model.entity.OnlineMeeting;
import com.format_project.meeting.model.vo.InvitationEmail;

public interface OnlineMeetingService {
    
    public Map<String,OnlineMeeting> requestUpcomingOnlineMeeting(String userId);
    // public ArrayList<OnlineMeeting> requestUpcomingOnlineMeeting(String userId);
    public Map<String,OnlineMeeting> refreshUpcomingOnlineMeeting(String userId);
    public void requestInvitationWithEmail(InvitationEmail invitationEmail);
}
