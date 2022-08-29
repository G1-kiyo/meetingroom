package com.format_project.meeting.bean.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ArrayTypeHandler extends BaseTypeHandler<ArrayList<SimpleGrantedAuthority>>{

    @Override
    public ArrayList<SimpleGrantedAuthority> getNullableResult(ResultSet arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        String role = arg0.getString(arg1);
        // 输入的数据应该用逗号分隔
        String[] roleList = role.split(",");
        for(int i=0;i<roleList.length;i++){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleList[i]);
            authorities.add(authority);
        }

        return authorities;
    }

    @Override
    public ArrayList<SimpleGrantedAuthority> getNullableResult(ResultSet arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        String role = arg0.getString(arg1);
        // 输入的数据应该用逗号分隔
        String[] roleList = role.split(",");
        for(int i=0;i<roleList.length;i++){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleList[i]);
            authorities.add(authority);
        }

        return authorities;
        
    }

    @Override
    public ArrayList<SimpleGrantedAuthority> getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        String role = arg0.getString(arg1);
        // 输入的数据应该用逗号分隔
        String[] roleList = role.split(",");
        for(int i=0;i<roleList.length;i++){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleList[i]);
            authorities.add(authority);
        }

        return authorities;
    }

    @Override
    public void setNonNullParameter(PreparedStatement arg0, int arg1, ArrayList<SimpleGrantedAuthority> arg2,
            JdbcType arg3) throws SQLException {
        // TODO Auto-generated method stub
        String[] str = new String[arg2.size()];
        int index = 0;
        for(SimpleGrantedAuthority simpleGrantedAuthority:arg2){
           str[index] = simpleGrantedAuthority.getAuthority();
           index = index+1;
        }
        arg0.setString(arg1, str.toString());
        
    }
    
}
