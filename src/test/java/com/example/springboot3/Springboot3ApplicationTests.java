package com.example.springboot3;

import com.example.springboot3.entity.Folder;
import com.example.springboot3.repository.FolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@SpringBootTest
class Springboot3ApplicationTests {

    @Autowired
    FolderRepository folderRepository;

    //学习/极客时间专栏/极客时间专栏/01-专栏课/35-面试现场/
    //学习/极客时间专栏/极客时间专栏/01-专栏课/34-技术与商业案例解读/
    //学习/极客时间专栏/极客时间专栏/01-专栏课/130-罗剑锋的C  实战笔记/
    //学习/极客时间专栏/极客时间专栏/01-专栏课/102-现代C  实战30讲/
    //视频/纪录片/BBC纪录片/冰冻星球/
    @Test
    void contextLoads() throws UnsupportedEncodingException {
    }

}
