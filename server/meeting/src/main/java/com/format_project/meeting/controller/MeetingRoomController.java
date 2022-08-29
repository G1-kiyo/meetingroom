package com.format_project.meeting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.mapper.MeetingRoomMapper;
import com.format_project.meeting.mapper.OfflineMeetingMapper;
import com.format_project.meeting.model.entity.MeetingRoom;
import com.format_project.meeting.model.entity.OfflineMeeting;
import com.format_project.meeting.service.impl.MeetingRoomServiceImpl;

@RestController
@RequestMapping(value = "/meeting_room")
public class MeetingRoomController {
    @Autowired
    MeetingRoomMapper meetingRoomMapper;
    @Autowired
    OfflineMeetingMapper offlineMeetingMapper;
    @Autowired
    MeetingRoomServiceImpl meetingRoomServiceImpl;

    @RequestMapping(value = "/basic_info")
    public String requestMeetingRoomInfo(@Param("currentDate") String currentDate) {
        Map<String, Map<String, MeetingRoom>> roomInfoMap = meetingRoomServiceImpl.loadMeetingRoomInfo(currentDate);
        return new JsonResult<Map<String, Map<String, MeetingRoom>>>(Code.REQUEST_SUCCESS, roomInfoMap).toJSON();
    }

    @RequestMapping(value = "/specific_info")
    public String requestDayMeetingInfo(@Param("roomId") String roomId, @Param("date") String date) {
        Map<String, ArrayList<Map<String,Object>>> result = meetingRoomServiceImpl.loadDayMeetingInfo(roomId, date);
        return new JsonResult<Map<String, ArrayList<Map<String,Object>>>>(Code.REQUEST_SUCCESS, result).toJSON();
    }

    @RequestMapping(value="/specific_info_month")
    public String requestMonthMeetingInfo(@Param("roomId") String roomId, @Param("date") String date){
        Map<String,ArrayList<OfflineMeeting>> result = meetingRoomServiceImpl.loadMonthMeetingInfo(roomId, date);
        return new JsonResult<Map<String,ArrayList<OfflineMeeting>>>(Code.REQUEST_SUCCESS, result).toJSON();
    }

    @RequestMapping(value = "/book_info")
    public String uploadRoomBookInfo(@RequestBody OfflineMeeting offlineMeeting, HttpServletResponse res) {
        boolean result = meetingRoomServiceImpl.storeRoomBookInfo(offlineMeeting);
        if(result==true){
            return new JsonResult<>(Code.BOOK_SUCCESS).toJSON();
        }else{
            return new JsonResult<>(Code.BOOK_FAILURE).toJSON();
        }
        
    }

}
