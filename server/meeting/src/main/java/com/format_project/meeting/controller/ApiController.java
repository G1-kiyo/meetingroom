package com.format_project.meeting.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.format_project.meeting.bean.FileProcessing;
import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.TimerUtil;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.bean.Code.CodeUtil;
import com.format_project.meeting.mapper.PwdResetMapper;
import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.dto.AuthInfo;
import com.format_project.meeting.service.impl.ApiServiceImpl;
import com.format_project.meeting.service.impl.UserDetailsServiceImpl;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api")
public class ApiController {
    @Autowired
    ObjectMapper objMapper;
    @Autowired
    UserMapper usermapper;
    @Autowired
    PwdResetMapper pwdResetMapper;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    CodeUtil codeUtil;
    @Autowired
    FileProcessing fileProcessing;
    @Autowired
    TimerUtil timerUtil;
    @Autowired
    ApiServiceImpl apiServiceImpl;

    HttpSession httpSession;
    // static Map<String, String> code = new HashMap<String, String>();

    @RequestMapping(value = "/avatar")
    // formdata中的文件，用MultipartFile类型接收
    public String avatarUpload(@Param("smfile") MultipartFile smfile, @Param("format") String format) throws Exception {
        // System.out.println(smfile);
        Map<String, Object> map = apiServiceImpl.storeAvatar(smfile, format);

        // 需要向下转型为boolean
        boolean check = (boolean) map.get("success");
        // System.out.println(check);
        if (check) {
            return new JsonResult<Map<String, Object>>(Code.UPLOAD_SUCCESS, map).toJSON();
        } else {
            String code = (String) map.get("code");
            // System.out.println(code);
            if (code.equals("image_repeated")) {
                return new JsonResult<Object>(Code.UPLOAD_REPEATED, map).toJSON();
            } else {
                return new JsonResult<Object>(Code.API_ACCESS_FAILURE, map).toJSON();
            }
        }

    }

    @RequestMapping(value = "/sendcode")
    public String generateCode(@Param("phone") String phone, HttpServletRequest req) {
        // 首先判断手机号码是否注册，如是，则保存相关信息，等待60s后注销
        
        Map<String, String> msg = apiServiceImpl.resetPwdByPhone(phone, req.getServletContext());
        return new JsonResult<Map<String, String>>(Code.CODE_SEND_SUCCESS, msg).toJSON();

    }

    // @Param用在mapper，@RequestParam用在controller
    // 如果不用，那么接受的参数名字需要与前端提交的json键名保持一致
    @RequestMapping(value = "/reset_pwd_phone")
    public String resetPasswordRequest(@RequestParam("phone") String phone, @RequestParam("code") String verificationCode,
            HttpServletRequest req,HttpServletResponse res) {
        // 将上传的资料中的手机号码与验证码和保存的信息进行对比
        // res.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
        String Expphone = (String) req.getServletContext().getAttribute("phone");
        String Expcode = (String) req.getServletContext().getAttribute("code");
        System.out.println(phone+" "+verificationCode);;
        if (Expphone != null && Expcode != null) {
            if (phone.equals(Expphone) && verificationCode.equals(Expcode)) {
                Map<String,String>result = new HashMap<String,String>();
                result.put("password_reset_uid", (String)req.getServletContext().getAttribute("uid"));
                return new JsonResult<Map<String,String>>(Code.VALIDATION_SUCCESS,result).toJSON();
            } else {
                return new JsonResult<>(Code.WRONG_VERIFICATION_CODE).toJSON();
            }
        } else {
            return new JsonResult<>(Code.CODE_EXPIRED).toJSON();
        }
    }

    @RequestMapping(value = "/reset_pwd_email")
    public String resetPasswordRequest(@Param("email") String email) {

        Map<String, String> msg = apiServiceImpl.resetPwdByEmail(email);
        return new JsonResult<Map<String, String>>(Code.EMAIL_SEND_SUCCESS, msg).toJSON();
        // return null;
    }

    @RequestMapping(value = "/validate_email")
    public String validateEmail(@Param("uid") String uid, HttpServletResponse res) {
        Map<String, Boolean> msg = apiServiceImpl.validatePwdResetEmail(uid);
        if (msg.get("link_status") == true) {
            return new JsonResult<Map<String, Boolean>>(Code.EMAIL_LINK_EXPIRED, msg).toJSON();
        } else {
            return new JsonResult<Map<String, Boolean>>(Code.EMAIL_LINK_ACTIVE, msg).toJSON();
        }
    }

    @RequestMapping(value = "/reset_pwd")
    public String resetPwd(@RequestBody AuthInfo authInfo, HttpServletRequest req, HttpServletResponse res) {
        apiServiceImpl.resetPassword(authInfo);
        return new JsonResult<>(Code.PASSWORD_RESET_SUCCESS).toJSON();

    }

}
