package com.example.springboot3.util;

import java.util.Map;

import com.example.springboot3.enums.FileType;

public class FileUtils {
    public static boolean isDirectory(String path){
        if(!path.contains(".")){
            return true;
        }
        String fileSuffix = path.substring(path.lastIndexOf("."));
        Map<String, String> map = FileType.map;
        if(map.get(fileSuffix)==null){
            return true;
        }
        return false;
    }
}

