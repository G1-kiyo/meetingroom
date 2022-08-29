package com.format_project.meeting.bean.Code;

import java.util.Random;

import org.springframework.stereotype.Component;


public enum Code {
    
    // 登录反馈
    LOGIN_SUCCESS(1000,"Login successfully!"),
    LOGIN_AUTHENTICATION_FAILURE(1001,"Cannot authenticate your account."),

    // 注册反馈
    REGISTER_SUCCESS(2000,"Register successfully!"),
    REGISTER_UPLOAD_FAILURE(2001,"Cannot upload your register information,please try again."),

    // 一般请求
    REQUEST_SUCCESS(2010,"Request successfully!"),
    REQUEST_FAILURE(2011,"Request failed."),

    // 认证反馈
    AUTHENTICATION_JWT_FAILURE(3001,"Something wrong with your JWT token."),
    USER_NOTFOUND(3002,"User is not found."),
    JWT_NOT_FOUND(3003,"Jwt token is not found."),
    JWT_VALIDATION_FAILURE(3004,"Jwt token failed to pass the validation."),

    // 上传反馈
    UPLOAD_SUCCESS(4000,"Upload successfully!"),
    UPLOAD_REPEATED(4001,"Upload already,please do not upload the avatar repeatedly!"),
    FILE_NOT_FOUND(4002,"File is not found."),

    // 数据传输反馈
    DATA_TRANSFER_ERROR(4003,"Data transfer error."),

    // 会议室预约反馈
    BOOK_SUCCESS(4010,"Book successfully!"),
    BOOK_FAILURE(4011,"Time Conflict."),

    // 用户信息获取反馈
    // USER_RETRIEVAL_FAILURE(4022,"Something worng happened in the user information retrieval process.")

    // 访问第三方接口
    API_ACCESS_FAILURE(5001,"fail to access the api."),

    // 请求密码重置反馈
    
    CODE_SEND_SUCCESS(6001,"Send code successfully!"),
    CODE_SEND_FAILURE(6002,"Fail to send code,please try again."),
    CODE_EXPIRED(6003,"Code has expired."),
    EMAIL_SEND_SUCCESS(6004,"Send email successfully!"),
    EMAIL_SEND_FAILURE(6005,"Fail to send email,please try again."),
    EMAIL_LINK_ACTIVE(6014,"Email link is still active"),
    EMAIL_LINK_EXPIRED(6015,"Email link has expired."),
    VALIDATION_SUCCESS(6016,"Validation success."),

    PASSWORD_RESET_SUCCESS(6023,"Reset password successfully!"),
    WRONG_VERIFICATION_CODE(6024,"Wrong verification code."),
    PASSWORD_RESET_FAILURE(6025,"Cannot reset your password.");

    
    private final int code;
    private final String msg;

    

    Code(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    
    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    

}
