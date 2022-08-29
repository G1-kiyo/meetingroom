package com.format_project.meeting.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.format_project.meeting.bean.Code.Code;



public class JsonResult<T> {
    int code;
    String message;
    String result;
    T data;
    ObjectMapper mapper = new ObjectMapper();
    Map<String,T> inputMap;
    Map<String,Object> outputMap = new HashMap<String,Object>();
    public JsonResult(){}
    public JsonResult(Code codeMark){
        this.code = codeMark.getCode();
        this.message = codeMark.getMsg();
    }
    public JsonResult(Code codeMark,T data){
        this.code = codeMark.getCode();
        this.message = codeMark.getMsg();
        this.data = data;
    }

    public String toJSON(){
        outputMap.put("code",code);
        outputMap.put("message",message);
        if(data != null)outputMap.put("data",data);
        try{
            result = mapper.writeValueAsString(outputMap);
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<String,T> readToObject(HttpServletRequest req,T obj){
        try(InputStream stream = req.getInputStream()){
            try{
                inputMap = mapper.readValue(stream, new TypeReference<Map<String,T>>(){});
                return inputMap;
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(IOException e){
            throw new Error("Something wrong with InputStream.");
        }
        return null;
    }

    public Map<String,T> stringJSONToObject(String json,T obj){
        try{
            inputMap = mapper.readValue(json,new TypeReference<Map<String,T>>(){});
            return inputMap;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}
