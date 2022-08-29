package com.format_project.meeting.bean.Code;

import java.util.Random;

import org.springframework.stereotype.Component;
@Component
public class CodeUtil {
    public String generateCode(int len){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer s = new StringBuffer();
        for(int i = 0; i < len; i++){

            Random randomChoice = new Random();
            int num = randomChoice.nextInt(3);
            switch(num){
                case 0:
                    int index0 = randomChoice.nextInt(26)+0;
                    System.out.println(str.charAt(index0));;
                    s.append(str.charAt(index0));
                    break;
                case 1:
                    int index1 = randomChoice.nextInt(26)+26;
                    s.append(str.charAt(index1));
                    break;
                case 2:
                    int index2 = randomChoice.nextInt(9)+52;
                    s.append(str.charAt(index2));
                    break;
                default:break;
            }
            // System.out.println(i);
        }
        
        return s.toString();
    }
}
