package com.format_project.meeting.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
// 项目启动后会自动读取配置
public class CorsConfig {
    
    @Bean
    // 声明一个Spring bean
    public WebMvcConfigurer corsconfigurer(){
        WebMvcConfigurer corsConfigurer = new WebMvcConfigurer(){
            
            @Override
            // 重写WebMvcConfigurer接口类的方法
            public void addCorsMappings(CorsRegistry registry){
                // 注册所有根目录下的路径
                // register.addMapping返回的是CorsRegistration类型
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowedOriginPatterns("*")
                        // 数据类型是Long
                        .maxAge(3*60*60L);
            }
        };
        return corsConfigurer;

    }


}