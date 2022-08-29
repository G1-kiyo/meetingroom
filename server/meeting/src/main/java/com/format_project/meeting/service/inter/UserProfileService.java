package com.format_project.meeting.service.inter;

import java.util.ArrayList;
import java.util.Map;

import com.format_project.meeting.model.dto.RoomBookDetail;
import com.format_project.meeting.model.entity.OfflineMeeting;

public interface UserProfileService {
    
    public ArrayList<OfflineMeeting> loadMyRoomBookInfo(String userId,String date);
    public Map<String,RoomBookDetail> loadRoomBookDetail(String meetingId);
    public void removeMyRoomBookInfo(String meetingId);
    public void refreshMyRoomBookInfo(OfflineMeeting offlineMeeting);
}
