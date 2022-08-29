package com.format_project.meeting.bean.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;

import com.format_project.meeting.mapper.UserMapper;
import com.format_project.meeting.model.entity.User;


// 估计只能进行类型转换，不能再次调用mapper获取数据库的内容
public class ArrayMemberTypeHandler extends BaseTypeHandler<ArrayList<User>>{

    @Autowired
    UserMapper userMapper;

    @Override
    public void setNonNullParameter(PreparedStatement arg0, int arg1, ArrayList<User> arg2, JdbcType arg3)
            throws SQLException {
        StringBuilder str = new StringBuilder();
        for(User user : arg2){
            str.append(user.getUserId()+",");
        }
        String data = str.toString();
        System.out.println(data);
        arg0.setString(arg1, data.substring(0,data.length()-1));
        // TODO Auto-generated method stub
        
    }

    @Override
    public ArrayList<User> getNullableResult(ResultSet arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub
        String userList = arg0.getString(arg1);
        String[] newUserList = userList.split(",");
        ArrayList<User> result = new ArrayList<User>();
        result = userMapper.findUserAll();
        System.out.println("this is"+result);
        return result;
    }

    @Override
    public ArrayList<User> getNullableResult(ResultSet arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        String userList = arg0.getString(arg1);
        String[] newUserList = userList.split(",");
        ArrayList<User> result = userMapper.findUserAll();
        return result;
    }

    @Override
    public ArrayList<User> getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        String userList = arg0.getString(arg1);
        String[] newUserList = userList.split(",");
        ArrayList<User> result = userMapper.findUserAll();
        return result;
    }
    
}
