package com.format_project.meeting.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AccountException;
import javax.servlet.http.HttpServletResponse;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.exception.AccountExistedException;
import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.entity.User;
import com.format_project.meeting.service.impl.UserDetailsServiceImpl;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
// 结合了@Controller和@ResponseBody，在该Controller类定义的方法返回的类型都是HttpResponse
// 只有@Controller，没有@ResponseBody，不能返回值
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/register")
    public String register(@RequestBody User user)throws AccountExistedException {
        boolean result = userDetailsServiceImpl.storeUserInfo(user);
        if(result){
            return new JsonResult<>(Code.REGISTER_SUCCESS).toJSON();
        }else{
            throw new AccountExistedException();
            // return new JsonResult<>(Code.REGISTER_UPLOAD_FAILURE).toJSON();
        }
        
    }


    @RequestMapping(value = "/job_info")
    // @Param只能接收使用get方法的请求
    public String requestJobInfo(@Param("userId") String userId) {

        Map<String, User> result = userDetailsServiceImpl.loadUserByUserId(userId);
        return new JsonResult<Map<String, User>>(Code.REQUEST_SUCCESS, result).toJSON();
    }
    @RequestMapping(value="/all_member")
    public String requestAllMember(){
        Map<String,ArrayList<User>> result = userDetailsServiceImpl.loadAllUser();
        return new JsonResult<Map<String,ArrayList<User>>>(Code.REQUEST_SUCCESS, result).toJSON();
    }
    @RequestMapping(value="/update_info")
    public String updateUserInfo(@RequestBody User user){
        userDetailsServiceImpl.refreshSomeUserInfo(user);
        return new JsonResult<>(Code.REQUEST_SUCCESS).toJSON();
    }

}
