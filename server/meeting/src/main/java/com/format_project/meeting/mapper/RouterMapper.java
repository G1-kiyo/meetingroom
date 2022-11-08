package com.format_project.meeting.mapper;

import java.util.ArrayList;

import com.format_project.meeting.model.vo.Router;

public interface RouterMapper {
    public ArrayList<Router> selectDynamicRoutes();
}
