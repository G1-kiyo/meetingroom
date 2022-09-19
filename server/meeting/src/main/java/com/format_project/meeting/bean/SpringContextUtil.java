package com.format_project.meeting.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub

        if(SpringContextUtil.applicationContext==null){
            SpringContextUtil.applicationContext = applicationContext;
        }
        
        
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    // 第一个<T>代表返回值泛型 第二个T代表返回值类型
    public static <T> T getBean(Class<T>clazz){
        return (T)SpringContextUtil.getBean(clazz);
    }
    
}
