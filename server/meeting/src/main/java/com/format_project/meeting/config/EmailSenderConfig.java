package com.format_project.meeting.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailSenderConfig  {
    
    @Bean
    // 这个mailsender需要spring.mail和spring.mail.properties前缀
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
        mailsender.setHost("smtp.163.com");
        mailsender.setUsername("xtgfzh202102@163.com");
        mailsender.setPassword("CWXSQDERXZRUYDWS");
        mailsender.setPort(465);

        Properties prop = mailsender.getJavaMailProperties();
        prop.setProperty("mail.smtp.ssl.enable", "true");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("smtp.starttls.enable", "true");
        prop.setProperty("smtp.starttls.required", "true");
        return mailsender;
    }
}
