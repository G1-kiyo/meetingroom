package com.format_project.meeting.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.format_project.meeting.model.entity.MeetingRoom;

@Mapper
public interface MeetingRoomMapper {
    
    public ArrayList<MeetingRoom> selectAllMeetingRoomInfo();
    public MeetingRoom selectMeetingRoomInfoByRoomId(String roomId);
    
    public void updateMeetingRoomStatus(@Param("roomId")String roomId,@Param("status")Long status);
}
