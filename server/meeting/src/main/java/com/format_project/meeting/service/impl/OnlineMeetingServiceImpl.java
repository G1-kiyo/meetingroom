package com.format_project.meeting.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.format_project.meeting.bean.Cache;
import com.format_project.meeting.mapper.OnlineMeetingMapper;
import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.entity.OnlineMeeting;
import com.format_project.meeting.model.entity.User;
import com.format_project.meeting.service.inter.OnlineMeetingService;

@Service
public class OnlineMeetingServiceImpl implements OnlineMeetingService {

    @Autowired
    OnlineMeetingMapper onlineMeetingMapper;

    @Override
    public Map<String, OnlineMeeting> requestUpcomingOnlineMeeting(String userId) {
        // TODO Auto-generated method stub
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(new Date());
            // System.out.println(SecurityContextHolder.getContext().getAuthentication());
            // User user =
            // (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // String userId = user.getUserId();
            Map<String, OnlineMeeting> meetingMap = new HashMap<String, OnlineMeeting>();
            ArrayList<OnlineMeeting> onlineMeeting = onlineMeetingMapper.loadUpcomingOnlineMeeting(date, userId);
            for (OnlineMeeting meeting : onlineMeeting) {
                meetingMap.put(meeting.getMeetingId(), meeting);
            }
            System.out.println(meetingMap.get("33567"));
            return meetingMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }

    @Override
    public Map<String, OnlineMeeting> refreshUpcomingOnlineMeeting(String userId) {
        // TODO Auto-generated method stub
        Map<String, OnlineMeeting> result = new HashMap<String, OnlineMeeting>();
        Map<String, OnlineMeeting> newOnlineMeeting = this.requestUpcomingOnlineMeeting(userId);
        for (String meetingId : newOnlineMeeting.keySet()) {
            if (!Cache.upcomingOnlineMeetingCache.containsKey(meetingId)) {
                // 返回新增的数据
                result.put(meetingId, newOnlineMeeting.get(meetingId));
            }
        }
        // 与此同时，更新共享缓存（这样可以及时更新，包括删除数据的情况）
        Cache.upcomingOnlineMeetingCache = newOnlineMeeting;

        return result;
    }

}
