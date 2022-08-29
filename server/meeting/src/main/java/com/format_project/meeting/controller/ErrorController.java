package com.format_project.meeting.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 处理非controller抛出的异常（如filter）
@RestController
@RequestMapping(value="/error")
public class ErrorController extends BasicErrorController{

    // public void handleError(HttpServletRequest req)throws Throwable{
    //     if(req.getAttribute("javax.servlet.error.exception")!=null){
    //         System.out.println(111);
    //         throw (Throwable)req.getAttribute("javax.servlet.error.exception");
    //     }
    // }

    // public ErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
    //     super(errorAttributes, errorProperties);
    //     //TODO Auto-generated constructor stub
    // }
    // public ErrorController() {
    //     super(new DefaultErrorAttributes(), new ErrorProperties());
    // }
    public ErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }



    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        // 获取错误信息
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        System.out.println(body);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",body.get("status"));
        map.put("message",body.get("message"));
        return new ResponseEntity<Map<String, Object>>(map, status);
    }
    
}
