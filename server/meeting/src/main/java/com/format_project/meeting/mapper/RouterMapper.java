package com.format_project.meeting.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.format_project.meeting.model.vo.Router;

@Mapper
public interface RouterMapper {
    public ArrayList<Router> selectDynamicRoutes();
}
