package com.format_project.meeting.bean.LoginAuthentication;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // TODO Auto-generated method stub
        response.setContentType("application/json;charset=utf-8");
        String msg = new JsonResult<String>(Code.LOGIN_AUTHENTICATION_FAILURE).toJSON();
        response.getWriter().write(msg);
        
    }
    
}
