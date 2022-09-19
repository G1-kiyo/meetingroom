package com.format_project.meeting.bean.LoginAuthentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.bean.JWTToken.JwtBean;
import com.format_project.meeting.model.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler  {

    @Autowired
    JwtBean jwtBean;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub
        response.setContentType("application/json;charset=utf-8");
        // response.sendRedirect("/user/index");
        Map<String,String> authInfo = new HashMap<String,String>();
        User userDetails = (User) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("this is"+SecurityContextHolder.getContext().getAuthentication());
        // 还需要将token值传回给客户端
        authInfo.put("token",jwtBean.generateJwt(authentication));
        authInfo.put("userId",userDetails.getUserId());
        String msg = new JsonResult<Map<String,String>>(Code.LOGIN_SUCCESS,authInfo).toJSON();
        // String msg = new JsonResult<String>(Code.LOGIN_SUCCESS).toJSON();
        response.getWriter().write(msg);
    }
    
}
