package com.format_project.meeting.service.inter;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.format_project.meeting.model.dto.AuthInfo;
import com.github.mizosoft.methanol.MultipartBodyPublisher;

public interface ApiService {
    
    public Map<String, Object> storeAvatar(MultipartFile smfile,String format)throws Exception;
    public Map<String, Object> accessApi(String url,MultipartBodyPublisher data,String... headers)throws Exception;
    public Map<String, String> resetPwdByPhone(String phone,ServletContext servletContext);
    public Map<String, String> resetPwdByEmail(String email);
    public void sendVerificationEmail(String to,String username,String uid);
    public Map<String, Boolean> validatePwdResetEmail(String uid);
    public void resetPassword(AuthInfo authInfo);
}
