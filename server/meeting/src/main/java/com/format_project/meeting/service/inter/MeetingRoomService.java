package com.format_project.meeting.service.inter;

import java.util.ArrayList;
import java.util.Map;

import com.format_project.meeting.model.entity.MeetingRoom;
import com.format_project.meeting.model.entity.OfflineMeeting;

public interface MeetingRoomService {
    
    public Map<String, Map<String, MeetingRoom>> loadMeetingRoomInfo(String currentDate);
    public Map<String, ArrayList<Map<String,Object>>> loadDayMeetingInfo(String roomId,String date);
    public Map<String,ArrayList<OfflineMeeting>> loadMonthMeetingInfo(String roomId,String date);
    public boolean storeRoomBookInfo(OfflineMeeting offlineMeeting);
}
