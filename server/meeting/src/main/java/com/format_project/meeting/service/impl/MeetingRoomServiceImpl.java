package com.format_project.meeting.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.format_project.meeting.mapper.MeetingRoomMapper;
import com.format_project.meeting.mapper.OfflineMeetingMapper;
import com.format_project.meeting.model.entity.MeetingRoom;
import com.format_project.meeting.model.entity.OfflineMeeting;
import com.format_project.meeting.service.inter.MeetingRoomService;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    @Autowired
    MeetingRoomMapper meetingRoomMapper;
    @Autowired
    OfflineMeetingMapper offlineMeetingMapper;

    @Override
    public Map<String, Map<String, MeetingRoom>> loadMeetingRoomInfo(String currentDate) {
        // TODO Auto-generated method stub
        // 获得当前日期的不同会议室房间的预约数，并及时更新状态

        ArrayList<Map<String, Object>> meetingCount = offlineMeetingMapper.OfflineMeetingCountByDate(currentDate);
        System.out.println(meetingCount);
        for (Map<String, Object> item : meetingCount) {

            meetingRoomMapper.updateMeetingRoomStatus((String) item.get("roomId"), 24 - (Long) item.get("count"));
        }
        Map<String, MeetingRoom> roomInfo = new HashMap<String, MeetingRoom>();
        Map<String, Map<String, MeetingRoom>> roomInfoMap = new HashMap<String, Map<String, MeetingRoom>>();
        ArrayList<MeetingRoom> room = meetingRoomMapper.selectAllMeetingRoomInfo();
        // 创建roomId为key的meetingroom信息map
        for (MeetingRoom item : room) {
            roomInfo.put(item.getRoomId(), item);
        }
        // 再将上述map放入以meetingroomInfo为key的map中，方便前端获取
        roomInfoMap.put("meetingroomInfo", roomInfo);
        return roomInfoMap;
    }

    @Override
    public Map<String, ArrayList<Map<String,Object>>> loadDayMeetingInfo(String roomId, String date) {
        // TODO Auto-generated method stub
        ArrayList<Map<String,Object>> meetingInfo = offlineMeetingMapper.selectDayMeetingByRD(roomId, date);
        Map<String, ArrayList<Map<String,Object>>> meetingMap = new HashMap<String, ArrayList<Map<String,Object>>>();
        meetingMap.put("dayMeetingInfo",meetingInfo);
        return meetingMap;
        
    }

    @Override
    public Map<String,ArrayList<OfflineMeeting>> loadMonthMeetingInfo(String roomId,String date){
        Map<String,ArrayList<OfflineMeeting>> result = new HashMap<String,ArrayList<OfflineMeeting>>();
        ArrayList<OfflineMeeting> meetingInfo = offlineMeetingMapper.selectMonthMeetingByRM(roomId, date.substring(5,7));
        for(OfflineMeeting item : meetingInfo){
            Date meetingDate = item.getMeetingDate();
            // 日期格式化工具
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String meetingKey = format.format(meetingDate);
            if(result.get(meetingKey)!=null){
                result.get(meetingKey).add(item);
            }else{
                ArrayList<OfflineMeeting> meetingList = new ArrayList<OfflineMeeting>();
                meetingList.add(item);
                result.put(meetingKey, meetingList);
            }
            
        }
        return result;
    }

    @Override
    public boolean storeRoomBookInfo(OfflineMeeting offlineMeeting) {
        // TODO Auto-generated method stub
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = format.format(offlineMeeting.getStartTime());
        String endTime = format.format(offlineMeeting.getEndTime());
        ArrayList<OfflineMeeting> repeatedmeeting = offlineMeetingMapper.selectMeetingBetweenTime(offlineMeeting.getRoomId(),startTime, endTime);
        if(repeatedmeeting.isEmpty()){
            offlineMeetingMapper.insertOfflineMeetingInfo(offlineMeeting);
            return true;
        }else{
            return false;
        }
    
    }

}
