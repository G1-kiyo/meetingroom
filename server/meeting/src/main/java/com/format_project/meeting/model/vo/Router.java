package com.format_project.meeting.model.vo;

import java.util.ArrayList;

public class Router {
    private String cid;
    private String name;
    private String path;
    private ArrayList<String> auth;
    private String parentId;
    private Boolean hasChildren;
    private Boolean view;
    private Boolean props;
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public ArrayList<String> getAuth() {
        return auth;
    }
    public void setAuth(ArrayList<String> auth) {
        this.auth = auth;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public Boolean getHasChildren() {
        return hasChildren;
    }
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    public Boolean getView() {
        return view;
    }
    public void setView(Boolean view) {
        this.view = view;
    }
    public Boolean getProps() {
        return props;
    }
    public void setProps(Boolean props) {
        this.props = props;
    }
    
    
}
