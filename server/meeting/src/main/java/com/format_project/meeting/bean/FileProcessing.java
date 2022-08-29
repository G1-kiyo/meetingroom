package com.format_project.meeting.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileProcessing {
    // 将输入流读成字符串
    public String streamToString(InputStream in){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // GZIPInputStream zip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len=in.read(buffer))>=0){
                // write中第一个参数为数据，第二个参数为offset起始位置，第三个参数为字节数
                out.write(buffer,0,len);
            }
            return out.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    // MultipartFile转换为File
    public File mutipartfileToFile(MultipartFile multipartfile){
        File file = new File(multipartfile.getOriginalFilename());
        try{
            InputStream in = multipartfile.getInputStream();
            OutputStream out = new FileOutputStream(file);
            int len = 0;
            byte[] data = new byte[1024];
            while((len=in.read(data))>=0){
                out.write(data,0,len);
            }
            // 记得要关闭流
            in.close();
            out.close();
            return file;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
