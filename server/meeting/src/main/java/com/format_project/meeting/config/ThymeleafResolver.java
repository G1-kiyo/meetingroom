package com.format_project.meeting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ThymeleafResolver {
    
    @Bean
    @ConfigurationProperties(prefix="spring.thymeleaf")
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
        springResourceTemplateResolver.setCacheable(true);
        springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
        springResourceTemplateResolver.setPrefix("classpath:/templates/");
        springResourceTemplateResolver.setSuffix(".html");
        return springResourceTemplateResolver;
    }

    @Bean("stringTemplateResolver")
    public StringTemplateResolver getStringTemplateResolver(){
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setCacheable(true);
        stringTemplateResolver.setTemplateMode(TemplateMode.HTML);
        return stringTemplateResolver;
    }

    // @Bean
    // public SpringTemplateEngine springTemplateEngine(){
    //     SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
    //     return springTemplateEngine;
    // }
}
