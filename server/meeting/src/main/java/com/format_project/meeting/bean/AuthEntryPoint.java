package com.format_project.meeting.bean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.format_project.meeting.bean.Code.Code;

public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    // 当authentication Object抛出AuthenticationException时调用
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // TODO Auto-generated method stub
        response.getWriter().write(new JsonResult<String>(Code.AUTHENTICATION_JWT_FAILURE).toJSON());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,Code.AUTHENTICATION_JWT_FAILURE.getMsg());
        
    }
    
}
