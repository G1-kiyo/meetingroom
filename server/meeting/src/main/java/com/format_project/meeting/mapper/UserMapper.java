package com.format_project.meeting.mapper;


import com.format_project.meeting.model.entity.MeetingRoom;
import com.format_project.meeting.model.entity.User;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Mapper
public interface UserMapper {
    // 接口类不需要声明属性和方法体
    // 针对mybatis框架，不需要实现mapper接口
    // 通过读取properties文件获得mapper xml文件路径（映射结果集与java POJO）
    // mapper xml文件中用id标识SQL语句，以此对应mapper java文件声明的方法；
    
    public User findUserByUsername(String username)throws UsernameNotFoundException;
    public User findUserByPhone(String phone)throws UsernameNotFoundException;
    public User findUserByEmail(String email)throws UsernameNotFoundException;
    public User findUserByUserId(String userId)throws UsernameNotFoundException;
    public ArrayList<User>findUserListByUserId(@Param("userList")ArrayList<User> userList);
    public ArrayList<User> findUserAll();
    
    public void insertUserAll(String username,String truename,String password,String phone,String email,String avatar);

    public void updatePasswordByPhone(@Param("phone")String phone,@Param("password")String password);
    public void updatePasswordByEmail(@Param("email")String email,@Param("password")String password);
    public void updateSomeUserInfo(User user);
    
    

}
