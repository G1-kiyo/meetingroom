package com.format_project.meeting.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.model.entity.OnlineMeeting;
import com.format_project.meeting.model.vo.InvitationEmail;
import com.format_project.meeting.service.impl.OnlineMeetingServiceImpl;

@RestController
@RequestMapping(value="/online_meeting")
public class OnlineMeetingController {

    @Autowired
    OnlineMeetingServiceImpl onlineMeetingServiceImpl;
    
    @RequestMapping(value="/upcoming")
    public String requestUpcomingOnlineMeeting(@RequestParam("date")String date,@RequestParam("userId")String userId){
        ArrayList<OnlineMeeting>onlineMeeting = onlineMeetingServiceImpl.requestOnlineMeetingList(userId);
        return new JsonResult<ArrayList<OnlineMeeting>>(Code.REQUEST_SUCCESS, onlineMeeting).toJSON();
    }
    @RequestMapping(value="/invite")
    public String requestInvitationWithEmail(@RequestBody InvitationEmail invitationEmail){
        onlineMeetingServiceImpl.requestInvitationWithEmail(invitationEmail);
        return new JsonResult<>(Code.EMAIL_SEND_SUCCESS).toJSON();
    }
    
}
