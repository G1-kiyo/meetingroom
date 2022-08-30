package com.format_project.meeting.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.format_project.meeting.bean.EmailSenderUtil;
import com.format_project.meeting.bean.FileProcessing;
import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.TimerUtil;
import com.format_project.meeting.bean.Code.CodeUtil;
import com.format_project.meeting.mapper.PwdResetMapper;
import com.format_project.meeting.model.dto.AuthInfo;
import com.format_project.meeting.model.dto.PwdReset;
import com.format_project.meeting.model.entity.User;
import com.format_project.meeting.service.inter.ApiService;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    FileProcessing fileProcessing;
    @Autowired
    ObjectMapper objMapper;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    PwdResetMapper pwdResetMapper;
    @Autowired
    TimerUtil timerUtil;
    @Autowired
    CodeUtil codeUtil;
    HttpSession localHttpSession;

    @Autowired
    EmailSenderUtil emailSenderUtil;
    @Autowired
    TemplateEngine templateEngine;
    @Value("${meeting.project.passwordResetAddr}")
    String addr;

    @Override
    public Map<String, Object> storeAvatar(MultipartFile smfile, String format) throws FileNotFoundException {
        // TODO Auto-generated method stub
        // 这里的headers是key-value值交替书写
        try {
            String[] headers = new String[] { "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36",
                    "Authorization", "viSFHvS30rScb2HSsiZS9LFDWlYId4fz", "Content-Type", "json" };
            File file = fileProcessing.mutipartfileToFile(smfile);
            // 上传图片到sm.ms图床，必须是multipart/form-data的Content-Type格式
            MultipartBodyPublisher multipartBodyPublisher = MultipartBodyPublisher.newBuilder()
                    .textPart("format", format).filePart("smfile", Path.of(file.getAbsolutePath())).build();
            Map<String, Object> map = accessApi("https://sm.ms/api/v2/upload", multipartBodyPublisher, headers);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Map<String, Object> accessApi(String url, MultipartBodyPublisher data, String... headers)
            throws IOException, InterruptedException {

        // TODO Auto-generated method stub
        // String...为字符串数组，这种展开运算符必须放在参数列表的最后
        // 建立客户端

        // HttpClient client = HttpClient.newHttpClient();
        Object obj = new Object();
        // Methanol client = Methanol.create();
        Methanol client = Methanol.newBuilder()
                .requestTimeout(Duration.ofSeconds(20))
                .readTimeout(Duration.ofSeconds(5))
                // .connectTimeout(Duration.ofSeconds(30))
                .build();
        MutableRequest request = MutableRequest.POST(url, data).headers(headers);

        // 建立请求

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create(url))
        // .headers(headers)
        // .POST(BodyPublishers.ofString(data))
        // .build();

        try {
            System.out.println("正在请求");
            // 获得响应
            // BodyHandlers用来规定httpresponse的返回值类型
            // 当请求的图片过大，服务器在读取图片文件，请求资源花费的时间较长，使得服务器最终返回connection reset的响应
            HttpResponse<String> res = client.send(request, BodyHandlers.ofString());
            // System.out.println(res);
            // readValue可以将输入流或字符串转为指定类型
            Map<String, Object> result = new JsonResult<Object>().stringJSONToObject(res.body(), obj);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Map<String, String> resetPwdByPhone(String phone, ServletContext servletContext) {
        // TODO Auto-generated method stub
        // 获取用户信息
        User userDetails = (User) userDetailsServiceImpl.loadUserByUsername(phone);
        // 将密码重置相关信息写入数据库
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // String uid = passwordEncoder.encode(userDetails.getUsername());
        String uid = codeUtil.generateCode(10);
        PwdReset pwdReset = new PwdReset();
        pwdReset.setUid(uid);
        pwdReset.setUserId(userDetails.getUserId());
        pwdReset.setExpired(false);
        pwdResetMapper.insertPwdResetInfo(pwdReset);
        // 设置定时器，时间一到，更新信息
        timerUtil.updatePwdResetInfo(uid, true, 60 * 1000);
        // 生成响应信息
        Map<String, String> msg = new HashMap<String, String>();
        msg.put("phone", userDetails.getPhone());
        msg.put("verification_code", (String) codeUtil.generateCode(6));

        // 保存会话，等待验证
        servletContext.setAttribute("uid", uid);
        servletContext.setAttribute("phone", msg.get("phone"));
        servletContext.setAttribute("code", msg.get("verification_code"));
        System.out.println("test" + servletContext.getAttribute("code"));
        String[] attrName = new String[] { "uid", "phone", "code" };
        timerUtil.clearSession(servletContext, attrName, 60 * 1000);
        return msg;
    }

    @Override
    public Map<String, String> resetPwdByEmail(String email) {
        // TODO Auto-generated method stub
        User userDetails = (User) userDetailsServiceImpl.loadUserByUsername(email);
        // 将密码重置相关信息存入数据库，发送密码重置邮件
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // String uid = passwordEncoder.encode(userDetails.getUsername());
        String uid = codeUtil.generateCode(10);
        PwdReset pwdResetObj = new PwdReset();
        pwdResetObj.setUid(uid);
        pwdResetObj.setUserId(userDetails.getUserId());
        pwdResetObj.setExpired(false);
        pwdResetMapper.insertPwdResetInfo(pwdResetObj);
        sendVerificationEmail(email, userDetails.getUsername(), uid);
        // 设置计时器，时间一到，更新状态
        timerUtil.updatePwdResetInfo(uid, true, 4 * 60 * 60 * 1000);
        // 发送uid
        Map<String, String> msg = new HashMap<String, String>();
        msg.put("password_reset_uid", uid);
        return msg;
    }

    @Override
    public void sendVerificationEmail(String to, String username, String uid) {
        // TODO Auto-generated method stub
        // springboot预定义的路径src/main/resources/static/、src/main/resources/public/存放静态资源
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password_reset_address", addr + "/" + uid);
        context.setVariable("button_text", "RESET YOUR PASSWORD");
        // 可以不用再对thymeleaf另行配置，autoconfiguration
        // 具体可查看https://www.jianshu.com/p/181d95693f1f
        // 解析器默认编码为utf-8
        String content = templateEngine.process("/verification_email.html", context);
        // String content = templateEngine.process("/verification_email.html", context);
        emailSenderUtil.sendMail(to, "PASSWORD RESET", content);
    }

    @Override
    public Map<String, Boolean> validatePwdResetEmail(String uid) {
        // TODO Auto-generated method stub
        PwdReset pwdResetObj = pwdResetMapper.selectInfoByUid(uid);
        Map<String, Boolean> msg = new HashMap<String, Boolean>();
        msg.put("link_status", pwdResetObj.isExpired());
        return msg;
    }

    @Override
    public void resetPassword(AuthInfo authInfo) {
        // TODO Auto-generated method stub
        User user = (User) userDetailsServiceImpl.loadUserByUsername(authInfo.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(authInfo.getPassword());
        pwdResetMapper.updatePwdByUserId(user.getUserId(), password);

    }

}
