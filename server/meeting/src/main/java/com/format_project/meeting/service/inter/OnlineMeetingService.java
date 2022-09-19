package com.format_project.meeting.service.inter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.format_project.meeting.model.entity.OnlineMeeting;

public interface OnlineMeetingService {
    
    public Map<String,OnlineMeeting> requestUpcomingOnlineMeeting(String userId);
    public Map<String,OnlineMeeting> refreshUpcomingOnlineMeeting(String userId);
}
