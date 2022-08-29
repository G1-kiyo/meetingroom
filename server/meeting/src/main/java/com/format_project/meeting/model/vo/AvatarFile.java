package com.format_project.meeting.model.vo;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class AvatarFile {
    
    private MultipartFile smfile;
    private String format;
    

    public MultipartFile getSmFile() {
        return smfile;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public void setSmFile(MultipartFile file) {
        this.smfile = file;
    }
    
}
