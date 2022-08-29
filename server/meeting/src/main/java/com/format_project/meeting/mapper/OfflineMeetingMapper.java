package com.format_project.meeting.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.format_project.meeting.model.dto.RoomBookDetail;
import com.format_project.meeting.model.entity.OfflineMeeting;

@Mapper
public interface OfflineMeetingMapper {
    // 在不确定属性值类型或属性值类型不同，可以用object类型接收
    // 返回多条记录时，用list或其他集合类型接收
    public ArrayList<Map<String,Object>> OfflineMeetingCountByDate(String date);

    public ArrayList<Map<String,Object>> selectDayMeetingByRD(@Param("roomId") String roomId,@Param("date") String date);
    public ArrayList<OfflineMeeting> selectMonthMeetingByRM(@Param("roomId")String roomId,@Param("month")String month);
    public ArrayList<OfflineMeeting> selectMyRoomBookInfo(@Param("userId")String userId,@Param("date")String date);
    public RoomBookDetail selectRoomBookDetail(String meetingId);
    public ArrayList<OfflineMeeting> selectMeetingBetweenTime(@Param("roomId")String roomId,@Param("startTime")String startTime,@Param("endTime") String endTime);

    public void insertOfflineMeetingInfo(OfflineMeeting offlineMeeting);

    public void deleteMyRoomBookInfo(String meetingId);

    public void updateMyRoomBookInfo(OfflineMeeting offlineMeeting);



}
