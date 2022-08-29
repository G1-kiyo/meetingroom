package com.format_project.meeting.bean;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.format_project.meeting.mapper.PwdResetMapper;

@Component
public class TimerUtil {
    @Autowired
    PwdResetMapper pwdResetMapper;
    public void clearSession(ServletContext servletContext,String[] attrName,int delay){
        final Timer timer = new Timer();
        // timer 有分为只执行一次（两个参数）和周期执行（三个参数），延迟一定时间再执行和立即执行（传入过去的时间Date）
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for(int i=0;i<attrName.length;i++){
                    servletContext.removeAttribute(attrName[i]);
                }
                timer.cancel();
            }
        }, delay);
    }
    public void updatePwdResetInfo(String uid,boolean isExpired,int delay){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                pwdResetMapper.updatePwdResetInfo(uid, isExpired);
                // TODO Auto-generated method stub
                
            }
        }, delay);
    }
}
