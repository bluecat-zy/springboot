package com.example.springboot3.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileType {

    public enum FileTypeEnum {
        // 文本文件
        TXT(".txt"),
        // 图像文件
        JPG(".jpg"),
        JPEG(".jpeg"),
        PNG(".png"),
        GIF(".gif"),
        BMP(".bmp"),
        // 音频文件
        MP3(".mp3"),
        WAV(".wav"),
        OGG(".ogg"),
        // 视频文件
        MP4(".mp4"),
        AVI(".avi"),
        MOV(".mov"),
        MKV(".mkv"),
        RMVB(".rmvb"),
        ASS(".ass"),
        SRT(".srt"),
        SSA(".ssa"),
        TS(".ts"),
        M4A(".m4a"),
        // Excel表格文件
        XLS(".xls"),
        XLSX(".xlsx"),
        // Excel文档文件
        DOC(".doc"),
        DOCX(".docx"),
        // PPt演示文稿文件
        PPT(".ppt"),
        PPTX(".pptx"),
        // pdf演示文稿文件
        PDF(".pdf"),
        // 可执行文件
        EXE(".exe"),
        // Windows动态链接库文件
        DLL(".dll"),
        // Windows批处理文件
        BAT(".bat"),
        // Windows配置文件
        CONFIG(".config"),
        // JavaScript脚本文件
        JAVASCRIPT(".js"),
        // CSS样式表文件
        CSS(".css"),
        // HTML网页文件
        HTML(".html"),
        // Markdown格式文件
        MARKDOWN(".md"),
        // 压缩文件
        ZIP(".zip"),
        RAR(".rar"),
        //电子出版物
        EPUB(".epub"),
        MOBI(".mobi"),
        URL(".url");


        private String type;

        private FileTypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public static final Map<String, String> map = new LinkedHashMap<>();

    static {
        FileTypeEnum[] values = FileTypeEnum.values();
        for (FileTypeEnum fileTypeEnum : values) {
            map.put(fileTypeEnum.getType(), fileTypeEnum.getType());
        }
    }
}
