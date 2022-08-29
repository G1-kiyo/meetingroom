package com.format_project.meeting.bean.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ArrayStringTypeHandler extends BaseTypeHandler<ArrayList<String>> {

    @Override
    public ArrayList<String> getNullableResult(ResultSet arg0, String arg1) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<String> result = new ArrayList<String>();
        String str = arg0.getString(arg1);
        // 输入的数据应该用逗号分隔
        if(str==null){
            result.add("null");
            return result;
        }
        String[] strList = str.split(",");
        for(int i=0;i<strList.length;i++){
            result.add(strList[i]);
        }
        return result;
    }

    @Override
    public ArrayList<String> getNullableResult(ResultSet arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<String> result = new ArrayList<String>();
        String str = arg0.getString(arg1);
        // 输入的数据应该用逗号分隔
        if(str==null){
            result.add("null");
            return result;
        }
        String[] strList = str.split(",");
        for(int i=0;i<strList.length;i++){
            result.add(strList[i]);
        }

        return result;
    }

    @Override
    public ArrayList<String> getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<String> result = new ArrayList<String>();
        String str = arg0.getString(arg1);
        // 输入的数据应该用逗号分隔
        if(str==null){
            result.add("null");
            return result;
        }
        String[] strList = str.split(",");
        for(int i=0;i<strList.length;i++){
            result.add(strList[i]);
        }

        return result;
    }

    @Override
    public void setNonNullParameter(PreparedStatement arg0, int arg1, ArrayList<String> arg2, JdbcType arg3)
            throws SQLException {
        // TODO Auto-generated method stub
        String[] str = new String[arg2.size()];
        int index = 0;
        for(String strItem:arg2){
           str[index] = strItem;
           index = index+1;
        }
        System.out.println("this is"+str.toString());
        arg0.setString(arg1, str.toString());
        
    }
    
}
