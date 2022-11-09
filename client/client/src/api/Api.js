import AxiosHttp from "../utils/AxiosHttp.js";

class Api{
    // 加载动态路由
    requestDynamicRoutes(requestData){
        return AxiosHttp.get('/api/router',requestData,false);
    }
    // 用户登录请求
    userLogin(requestData){
        return AxiosHttp.post('/user/login',requestData,true);
    }
    // 用户注册请求
    userRegister(requestData){
        return AxiosHttp.post('/user/register',requestData,true);
    }
    // 更新用户个人信息
    updateUserInfo(requestData){
        return AxiosHttp.post('/user/update_info',requestData,false);
    }

    // 上传头像
    uploadAvatar(requestData){
        return AxiosHttp.post('/api/avatar',requestData,true);
    }
    // 通过手机找回密码
    requestPwdResetWithPhone(requestData){
        return AxiosHttp.get('/api/reset_pwd_phone',requestData,true);
    }
    // 通过邮箱找回密码
    requestPwdResetWithEmail(requestData){
        return AxiosHttp.get('/api/reset_pwd_email',requestData,true);
    }
    // 发送验证码请求
    requestSendCode(requestData){
        return AxiosHttp.get('/api/sendcode',requestData,true);
    }
    // 验证邮箱链接
    validateEmailLink(requestData){
        return AxiosHttp.get('/api/validate_email',requestData,true);
    }
    // 提交密码重置表单
    submitPwdResetForm(requestData){
        return AxiosHttp.post('/api/reset_pwd',requestData,true);
    }

    // 请求用户工作信息
    requestUserJobInfo(requestData){
        return AxiosHttp.get('/user/job_info',requestData,true);
    }
    // 请求所有成员信息
    requestAllMember(requestData){
        return AxiosHttp.get('/user/all_member',requestData,false);
    }
    // 请求线下会议室信息
    requestMeetingRoomInfo(requestData){
        return AxiosHttp.get('/meeting_room/basic_info',requestData,true);
    }
    // 请求指定会议室信息(默认为当天)
    requestDayMeetingInfo(requestData){
        return AxiosHttp.get('/meeting_room/specific_info',requestData,true);
    }
    // 请求指定会议室的某月信息
    requestMonthMeetingInfo(requestData){
        return AxiosHttp.get('/meeting_room/specific_info_month',requestData,true)
    }
    // 请求个人所有会议预约信息
    requestMyRoomBookInfo(requestData){
        return AxiosHttp.get('/user/profile/request',requestData,true)
    }
    // 请求个人特定的会议预约具体细节
    requestRoomBookDetail(requestData){
        return AxiosHttp.post('user/profile/request_detail',requestData,true);
    }
    // 删除个人会议预约信息
    deleteMyRoomBookInfo(requestData){
        return AxiosHttp.post('/user/profile/delete_request',requestData,true)
    }
    // 更新个人会议预约信息
    updateMyRoomBookInfo(requestData){
        return AxiosHttp.post('/user/profile/update_request',requestData,false);
    }
    // 提交会议预约信息
    uploadRoomBookInfo(requestData){
        return AxiosHttp.post('/meeting_room/book_info',requestData,true);
    }

    // 请求线上会议信息
    requestUpcomingOnlineMeeting(requestData){
        return AxiosHttp.get('/online_meeting/upcoming',requestData,false);
    }
    // 请求预约线上会议
    requestScheduleOnlineMeeting(requestData){
        return AxiosHttp.post('/online_meeting/schedule',requestData,false);
    }
    // 请求线上即时会议唯一ID
    requestPersonalMeetingId(requestData){
        return AxiosHttp.get('/online_meeting/personal_info',requestData,false);
    // 请求发送邀请邮件
    }
    requestInvitationWithEmail(requestData){
        return AxiosHttp.post('/online_meeting/invite',requestData,false);
    }
}
export default new Api();