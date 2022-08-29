package com.format_project.meeting.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.entity.OfflineMeeting;
import com.format_project.meeting.model.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    // 直接注入mapper，就可以调用接口内的方法，不需要实例化
    public User user;
    public Collection<? extends GrantedAuthority> authorities;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 返回值是UserDetails，默认的只有username、password和authority
        // 可以自定义UserDetails
        boolean isEmail = Pattern.matches("^\\w+([-+.]\\w)*@\\w+([-.]\\w)*\\.\\w+([-.]\\w)*$", username);
        boolean isPhone = Pattern.matches("^(13[0-9]|14[5|7]|15[0-9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", username);
        try {
            if (isEmail) {
                user = userMapper.findUserByEmail(username);
            } else if (isPhone) {
                user = userMapper.findUserByPhone(username);
            }
            // try {
            // Collection<SimpleGrantedAuthority> authority = new
            // ArrayList<SimpleGrantedAuthority>();
            // System.out.println("Authority" + user.getAuthority());
            // authority.add(user.getAuthority());
            // authorities = authority;

            // } catch (Exception e) {
            // e.printStackTrace();
            // }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException(e.getMessage());
        }
        // System.out.println(new BCryptPasswordEncoder().encode("846239729abc"));
        return user;

    }

    public Map<String, User> loadUserByUserId(String userId) {
        User user = userMapper.findUserByUserId(userId);
        Map<String, User> userInfo = new HashMap<String, User>();
        userInfo.put("userJobInfo", user);
        return userInfo;
    }

    public Map<String, ArrayList<User>> loadAllUser() {
        ArrayList<User> userlist = userMapper.findUserAll();
        Map<String, ArrayList<User>> info = new HashMap<String, ArrayList<User>>();
        info.put("allMemberInfo", userlist);
        return info;
    }

    public boolean storeUserInfo(User user) {
        // TODO Auto-generated method stub
        if (userMapper.findUserByPhone(user.getPhone()) == null
                && userMapper.findUserByEmail(user.getEmail()) == null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userMapper.insertUserAll(
                    user.getUsername(),
                    user.getTruename(),
                    passwordEncoder.encode(user.getPassword()),
                    user.getPhone(),
                    user.getEmail(),
                    user.getAvatar());
            return true;
        }else{
            return false;
        }

    }

    public void refreshSomeUserInfo(User user) {
        userMapper.updateSomeUserInfo(user);
    }

}
