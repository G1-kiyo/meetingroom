package com.format_project.meeting.bean;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component
public class EmailSenderUtil {
    @Autowired
    JavaMailSender javaMailSender;
    // 读取properties文件
    @Value("${spring.mail.username}")
    String from;

    public void sendMail(String to,String subject,String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,"UTF-8");
        try{
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            // 表示所发送的文本内容是html格式的
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(message);
        }catch(MessagingException e){
            e.printStackTrace();
        }
        

    }
    public void sendMultipleMails(String[]to,String subject,String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,"UTF-8");
        try{
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            // 表示所发送的文本内容是html格式的
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(message);
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }
}
