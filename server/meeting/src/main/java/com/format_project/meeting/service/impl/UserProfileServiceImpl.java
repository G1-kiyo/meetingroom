package com.format_project.meeting.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.format_project.meeting.mapper.OfflineMeetingMapper;
import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.dto.RoomBookDetail;
import com.format_project.meeting.model.entity.OfflineMeeting;
import com.format_project.meeting.service.inter.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService{
    @Autowired
    OfflineMeetingMapper offlineMeetingMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public ArrayList<OfflineMeeting> loadMyRoomBookInfo(String userId, String date) {
        // TODO Auto-generated method stub
        ArrayList<OfflineMeeting> myRoomBookInfo = offlineMeetingMapper.selectMyRoomBookInfo(userId, date);
        return myRoomBookInfo;
    }
    @Override
    public Map<String,RoomBookDetail> loadRoomBookDetail(String meetingId) {
        // TODO Auto-generated method stub
        RoomBookDetail detail = offlineMeetingMapper.selectRoomBookDetail(meetingId);
        // 疑问本来在typehandler 中设定的数据类型是ArrayList<String>
        // 但映射到实体类的member属性为ArrayList<User> 却可以自动转换
        detail.setMember(userMapper.findUserListByUserId(detail.getMember()));
        Map<String,RoomBookDetail> result = new HashMap<String,RoomBookDetail>();
        result.put("roomBookDetail",detail);
        return result;
    }
    @Override
    public void removeMyRoomBookInfo(String meetingId) {
        // TODO Auto-generated method stub
        offlineMeetingMapper.deleteMyRoomBookInfo(meetingId);
        
    }
    @Override
    public void refreshMyRoomBookInfo(OfflineMeeting offlineMeeting) {
        // TODO Auto-generated method stub
        offlineMeetingMapper.updateMyRoomBookInfo(offlineMeeting);
    }
    
}
