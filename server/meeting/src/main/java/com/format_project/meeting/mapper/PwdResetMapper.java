package com.format_project.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.format_project.meeting.model.dto.PwdReset;

@Mapper
public interface PwdResetMapper {
    
    public void insertPwdResetInfo(PwdReset pwdReset);
    public void updatePwdResetInfo(@Param("uid")String uid,@Param("isExpired")boolean isExpired);

    public PwdReset selectInfoByUid(String uid);

    public void updatePwdByUserId(@Param("userId")String userId,@Param("password")String password);
}
