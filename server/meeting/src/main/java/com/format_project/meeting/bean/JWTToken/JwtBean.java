package com.format_project.meeting.bean.JWTToken;

import java.io.FileNotFoundException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.exception.JwtNotFoundException;
import com.format_project.meeting.exception.JwtValidationFailException;
import com.format_project.meeting.model.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.*;

@Component
public class JwtBean {

    @Value("${meeting.project.JwtTokenExpiration}")
    private int JwtTokenExpiration;
    @Value("${meeting.project.JwtTokenSecret}")
    private String JwtTokenSecret;



    public String generateJwt(Authentication auth) {
        User userDetails = (User) auth.getPrincipal();
        // System.out.println("generate"+userDetails.getUsername());
        // 这里用userId当作subject
        String token = Jwts.builder().setSubject(userDetails.getUserId()).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JwtTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, JwtTokenSecret).compact();

        return token;
    }

    public String getUsernameFromToken(String token){
        // 和generateToken的setSubject对应
        return Jwts.parser().setSigningKey(JwtTokenSecret).parseClaimsJws(token).getBody().getSubject();
    }
// throws语句是本身方法不处理异常，然后将该异常向上层层传递给调用者
// throws语句是抛出可能存在的异常，前提是该方法需要throw指明抛出的location
    public boolean validateJwt(String authToken)throws JwtValidationFailException{
        try {
            Jwts.parser().setSigningKey(JwtTokenSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            throw new JwtValidationFailException(Code.JWT_VALIDATION_FAILURE.getMsg());
        }
    }

    public String parseJwt(HttpServletRequest request)throws JwtNotFoundException{
        String jwtInfo = request.getHeader("Authorization");
        // System.out.println("token"+jwtInfo);
        if(StringUtils.hasText(jwtInfo)&&jwtInfo.startsWith("Bearer ")){
            return jwtInfo.substring(7);
        }else{
            throw new JwtNotFoundException(Code.JWT_NOT_FOUND.getMsg());
        }
        
    }
}
