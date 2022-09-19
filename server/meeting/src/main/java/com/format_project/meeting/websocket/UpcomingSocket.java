package com.format_project.meeting.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.format_project.meeting.bean.Cache;
import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.SpringContextUtil;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.model.entity.OnlineMeeting;
import com.format_project.meeting.service.impl.OnlineMeetingServiceImpl;

@ServerEndpoint("/ws/upcoming/{userId}")
@Component
// 将目前的类定义为一个websocket服务器端
public class UpcomingSocket {
    static OnlineMeetingServiceImpl onlineMeetingServiceImpl;

    @Autowired
    public void setOnlineMeetingServiceImpl(OnlineMeetingServiceImpl onlineMeetingServiceImpl){
        UpcomingSocket.onlineMeetingServiceImpl = onlineMeetingServiceImpl;
    }

    private Session session;
    private String userId;

    private static Map<String, UpcomingSocket> clients = new HashMap<String, UpcomingSocket>();

    public static Map<String, UpcomingSocket> getUpcomingSocketClients() {
        // 不能在static方法中使用this，因为当在其他类中引用该方法，是没有this的；
        return clients;
    }
    public String getUserId(){
        return userId;
    }


    @OnOpen
    // 除了session、endpointconfig类型的参数外，都应用@pathparam注解
    public void onOpen(Session session,@PathParam("userId")String userId) throws IOException {
        // 连接开始时，将session注册为当前类全局共享变量
        // 这样在sendmessage时才可以发送消息
        this.session = session;
        this.userId = userId;
        // System.out.println(session.getId());
        clients.put(session.getId(), this);
        System.out.println(userId);
        Map<String, OnlineMeeting> onlineMeeting = new HashMap<String, OnlineMeeting>();
        onlineMeeting = onlineMeetingServiceImpl.requestUpcomingOnlineMeeting(userId);
        Cache.upcomingOnlineMeetingCache = onlineMeeting;
        this.sendMessage(
                new JsonResult<Map<String, OnlineMeeting>>(Code.REQUEST_SUCCESS, Cache.upcomingOnlineMeetingCache)
                        .toJSON());

    }

    @OnMessage
    // 获取前端发送的数据
    public void onMessage(String data) throws IOException {
        if (data.equals("update")) {
            onlineMeetingServiceImpl.refreshUpcomingOnlineMeeting(userId);
        }

    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("close");
        if (Cache.upcomingOnlineMeetingCache != null && Cache.upcomingOnlineMeetingCache.size() > 0) {
            // 清除onlinemeeting记录
            Cache.upcomingOnlineMeetingCache.clear();
            // 清除客户端记录
            clients.remove(session.getId());
        }

    }

    public void sendMessage(String text) throws IOException {
        System.out.println("send"+text);
        this.session.getAsyncRemote().sendText(text);
    }

}
