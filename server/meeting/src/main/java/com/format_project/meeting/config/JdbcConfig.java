package com.format_project.meeting.config;

import javax.sql.DataSource;
import com.mysql.cj.jdbc.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 标注为配置类，Spring Boot会自动读取
public class JdbcConfig {
    
    @ConfigurationProperties(prefix="spring.datasource")
    @Bean
    // 可能是该datasource类会自动读取properties文件的前缀spring.datasource并自行配置？，
    // 可能换个前缀就不能自动配置了
    public DataSource dataSource(){
        MysqlDataSource mysql = new MysqlDataSource();
        return mysql;
    }
}
