package com.format_project.meeting.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.model.dto.RoomBookDetail;
import com.format_project.meeting.model.entity.OfflineMeeting;
import com.format_project.meeting.service.impl.UserProfileServiceImpl;

@RestController
@RequestMapping(value="/user/profile")
public class UserProfileController {
    @Autowired
    UserProfileServiceImpl userProfileServiceImpl;

    @RequestMapping(value="/request")
    public String requestMyRoomBookInfo(@RequestParam("userId") String userId,@RequestParam("date")String date){
        ArrayList<OfflineMeeting> result = userProfileServiceImpl.loadMyRoomBookInfo(userId, date);
        return new JsonResult<ArrayList<OfflineMeeting>>(Code.REQUEST_SUCCESS, result).toJSON();
    }
    // 接收formdata
    @RequestMapping(value="/request_detail",method=RequestMethod.POST)
    public String requestRoomBookDetail(String meetingId){
        Map<String,RoomBookDetail> detail = userProfileServiceImpl.loadRoomBookDetail(meetingId);
        return new JsonResult<Map<String,RoomBookDetail>>(Code.REQUEST_SUCCESS, detail).toJSON();
    }

    @RequestMapping(value="/delete_request",method=RequestMethod.POST)
    public String deleteMyRoomBookInfo(String meetingId){
        userProfileServiceImpl.removeMyRoomBookInfo(meetingId);
        return new JsonResult<>(Code.REQUEST_SUCCESS).toJSON();
    }
    
    @RequestMapping(value="/update_request")
    public String updateMyRoomBookInfo(OfflineMeeting offlineMeeting){
        userProfileServiceImpl.refreshMyRoomBookInfo(offlineMeeting);
        return new JsonResult<>(Code.REQUEST_SUCCESS).toJSON();
    }
}
