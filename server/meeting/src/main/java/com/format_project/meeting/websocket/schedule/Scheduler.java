package com.format_project.meeting.websocket.schedule;

import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.model.entity.OnlineMeeting;
import com.format_project.meeting.service.impl.OnlineMeetingServiceImpl;
import com.format_project.meeting.websocket.UpcomingSocket;

// @EnableScheduling
@Component
public class Scheduler {

    @Autowired
    OnlineMeetingServiceImpl onlineMeetingServiceImpl;

    @Scheduled(fixedRate = 4*2000)
    public void sendUpcomingOnlineMeeting() throws IOException {
        Map<String, UpcomingSocket> clients = UpcomingSocket.getUpcomingSocketClients();
        // 保证键唯一
        if (clients!=null && clients.size() > 0) {
            Set<String> keys = clients.keySet();
            for (String key : keys) {
                Map<String, OnlineMeeting> result = onlineMeetingServiceImpl.refreshUpcomingOnlineMeeting(clients.get(key).getUserId());
                String updateData = new JsonResult<Map<String, OnlineMeeting>>(Code.REQUEST_SUCCESS, result).toJSON();
                clients.get(key).sendMessage(updateData);
            }
        }

    }

}
