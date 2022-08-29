package com.format_project.meeting.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.format_project.meeting.bean.JsonResult;
import com.format_project.meeting.bean.Code.Code;
import com.format_project.meeting.model.entity.User;

@Component
@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
// 检测全局exception
public class ControllerAdvisor {
    
    // 对捕捉到的exception进行响应
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex){
        ex.printStackTrace();
        return new JsonResult<String>(Code.USER_NOTFOUND).toJSON();
    }

    @ExceptionHandler(AccountExistedException.class)
    public String handleAccountExistedException(AccountExistedException ex){
        System.out.println(1000);
        ex.printStackTrace();
        return new JsonResult<String>(Code.REGISTER_UPLOAD_FAILURE).toJSON();
    }


    @ExceptionHandler(JwtNotFoundException.class)
    public String handleJwtNotFoundException(JwtNotFoundException ex){
        ex.printStackTrace();
        return new JsonResult<String>(Code.JWT_NOT_FOUND).toJSON();
    }

    @ExceptionHandler(FileNotFoundException.class)
    public String handleFileNotFoundException(FileNotFoundException ex){
        ex.printStackTrace();
        return new JsonResult<String>(Code.FILE_NOT_FOUND).toJSON();
    }

    @ExceptionHandler(JwtValidationFailException.class)
    public String handleJwtValidationException(JwtValidationFailException ex){
        ex.printStackTrace();
        return new JsonResult<String>(Code.JWT_VALIDATION_FAILURE).toJSON();
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex){
        ex.printStackTrace();
        return new JsonResult<String>(Code.REQUEST_FAILURE).toJSON();
    }

    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException ex){
        ex.printStackTrace();
        return new JsonResult<String>(Code.DATA_TRANSFER_ERROR).toJSON();
    }
}
