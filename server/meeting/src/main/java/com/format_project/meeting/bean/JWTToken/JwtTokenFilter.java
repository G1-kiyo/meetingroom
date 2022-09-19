package com.format_project.meeting.bean.JWTToken;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.exception.JwtNotFoundException;
import com.format_project.meeting.exception.JwtValidationFailException;
import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.entity.User;
import com.format_project.meeting.service.impl.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtBean jwtBean;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // 从request获取token-->验证token，从token获取username
        // 构造安全上下文的验证token
        //
        String token = null;
        try {
            token = jwtBean.parseJwt(request);

            if (token != null && jwtBean.validateJwt(token)) {
                // String userId = jwtBean.getUsernameFromToken(token);
                // User userDetails = userMapper.findUserByUserId(userId);
                // System.out.println(userDetails.getEmail());
                // UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                //         userDetails.getUsername(), userDetails.getPassword());
                // SecurityContextHolder.getContext().setAuthentication(authToken);
                
            }
        } catch (JwtNotFoundException e) {
            // 如果用户尚处于登陆阶段，并未携带token，在经过这一filter时
            // 会因为token不存在而抛出异常，返回错误信息。这导致登录失败
            // 如果想控制用户某页面文件资源，可以让前端判断是否携带了token（即后端是否传回token）
            // throw new JwtNotFoundException(Code.JWT_NOT_FOUND.getMsg());
        } catch (JwtValidationFailException e) {
            // throw new JwtValidationFailException(Code.JWT_VALIDATION_FAILURE.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 得以调用下一个filter
        doFilter(request, response, filterChain);
    }

}
