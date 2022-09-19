package com.format_project.meeting.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.format_project.meeting.model.entity.OnlineMeeting;

@Mapper
public interface OnlineMeetingMapper {
    
    public ArrayList<OnlineMeeting> loadUpcomingOnlineMeeting(@Param("date")String date,@Param("userId")String userId);
}
