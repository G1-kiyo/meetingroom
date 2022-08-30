# meetingroom 在线会议室管理系统

## 系统定位
多功能的在线会议室管理系统，通过线上线下相结合的方式为企业员工创建畅通交流的条件。该系统简洁、友好、人性化的交互界面能够为用户提供良好的使用体验，提高工作效率，丰富交流方式。

## 系统功能
- [X] **用户管理**
  - [X] 用户注册
  - [X] 用户登录
  - [X] 找回密码
  - [X] 密码重置
    - [X] 模拟手机验证码找回（条件受限）
    - [X] 邮箱找回
  
- [ ] **个人中心**
  - [ ] 个人信息修改保存
  - [ ] 分享动态
  - [ ] 日程表
  - [X] 会议室预约情况
    - [X] 预约情况查询
    - [X] 预约情况修改保存
  
- [X] **会议室管理**
  - [X] 会议室信息查询
  - [X] 会议室预约（日/月视图）

- [ ] **线上会议室管理**
  - [ ] 对话管理
    - [ ] 视频通话
    - [ ] 语音通话
    - [ ] 文字聊天
  - [ ] 对话记录管理
    - [ ] 对话记录保存
    - [ ] 对话记录导出
  - [ ] 邀请
    - [ ] 复制链接
    - [ ] 复制邀请信息
    - [ ] 邮件邀请

- [ ] **会议文档管理**
  - [ ] 文档查询
  - [ ] 文档上传下载
  
- [ ] **管理员**
  - [ ] 黑名单管理
  - [ ] 会议室记录更新（增删改）
  
## 技术架构
|       |         架构            |
| :-----| :----------------------:|
| 前端  | Vue3++ElementPlus+Less  |
| 后端  | Spring Boot+MyBatis     |
| 数据库| MySQL8.0                |

## 使用步骤
1.先运行后端 
```
>>>cd server\meeting
>>>mvn spring-boot:run
```
2.后运行前端
```
>>>cd client\client
>>>npm run serve
```

## 效果预览
### 用户注册
![用户注册](https://github.com/G1-kiyo/meetingroom/blob/master/preview/1.gif)

### 密码重置
![密码重置](https://github.com/G1-kiyo/meetingroom/blob/master/preview/2.gif)

### 会议室信息查询
![会议室信息查询](https://github.com/G1-kiyo/meetingroom/blob/master/preview/3.gif)

### 会议室预约
![会议室预约](https://github.com/G1-kiyo/meetingroom/blob/master/preview/4.gif)

### 个人预约信息查询
![个人预约信息查询](https://github.com/G1-kiyo/meetingroom/blob/master/preview/5.gif)






  

    
  

