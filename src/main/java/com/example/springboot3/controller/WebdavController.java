package com.example.springboot3.controller;


import ch.qos.logback.core.util.FileUtil;
import com.example.springboot3.common.Result;
import com.example.springboot3.entity.File;
import com.example.springboot3.entity.Folder;
import com.example.springboot3.entity.User;
import com.example.springboot3.repository.FileRepository;
import com.example.springboot3.repository.FolderRepository;
import com.example.springboot3.util.FileUtils;
import com.example.springboot3.webdav.WebDavUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController("/webdav")
@Tag(name = "阿里云盘webdavController", description = "阿里云盘webdav")
public class WebdavController {

    @Value("${webdav.url}")
    public String url;
    @Autowired
    public WebDavUtils webDavUtils;
    @Autowired
    public FolderRepository folderRepository;
    @Autowired
    public FileRepository fileRepository;
    @Autowired
    CriteriaBuilder criteriaBuilder;

    @GetMapping("/list")
    @Operation(summary = "获取webdav当前文件下的所有文件")
    public Result list(@Parameter(name = "参数", description = "文件路径") String str) {
        List<String> list = null;
        try{
            if (str != null) {
                list = webDavUtils.list(url + str);
            }
        }catch(Exception e){
            Result.error(e.getMessage());
        }

        return Result.success(list);
    }

    @GetMapping("/listById")
    @Operation(summary = "根据文件夹id获取文件夹")
    public List<String> listById(@Parameter(name = "参数", description = "文件夹Id") Integer id) throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        List<String> list = null;
        if (id != null) {
            Optional<Folder> optional = folderRepository.findById(String.valueOf(id.intValue()));
            if (optional.isPresent()) {
                Folder folder = optional.get();
                String encodeFolderName = folder.getEncodeFolderName();
                encodeFolderName = encodeFolderName.replace("+", "%20");
                list = webDavUtils.list(url + encodeFolderName);
            }
        }

        return list;
    }


    @GetMapping("/listAll")
    @Operation(summary = "获取webdav当前文件下的所有文件")
    public List<String> listAll() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        List<String> list = webDavUtils.list(url);
        list =list.stream().filter(data->!data.equals("webdav/")).collect(Collectors.toList());
        for (String str : list) {
            str =str.replace("webdav/","");
            if (FileUtils.isDirectory(str)) {
                Folder folder = new Folder();
                folder.setFolderName(str);
                folder.setParentFolder("0");
                Folder save = folderRepository.save(folder);
            } else {
                File file = new File();
                if (str.contains("/")) {
                    file.setFileName(str.substring(str.lastIndexOf("/")));
                } else {
                    file.setFileName(str);
                }
                file.setFileType(str.substring(str.lastIndexOf(".")));
                file.setFolderId("0");
                fileRepository.save(file);
            }
        }
        return list;
    }


    @GetMapping("/listAll2")
    @Operation(summary = "获取webdav当前文件下的所有文件")
    public List<String> listAll2() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        Iterator<Folder> iterator = folderRepository.findAll().iterator();
        List<String> data = new LinkedList<>();
        while (iterator.hasNext()) {
            Folder next = iterator.next();
            String folderName = next.getFolderName();
            System.out.println(folderName);
            String encodeFolderName = next.getEncodeFolderName();
            if(encodeFolderName.contains("+")){
                encodeFolderName = encodeFolderName.replaceAll("\\+", "%20");
            }
            List<String> list=null;
            try{
                list = webDavUtils.list(url + encodeFolderName);
            }catch(Exception exception){
                System.out.println(folderName+"获取文件夹里的资源失败");
                exception.printStackTrace();
                continue;
            }
            List<String> collect = list.stream().filter(str -> !str.replace("webdav/","").equals(folderName)).collect(Collectors.toList());
            for (String str : collect) {
                str=str.replace("webdav/","");
                data.add(folderName + "/" + str);
                if (FileUtils.isDirectory(str)) {
                    int result = folderRepository.findByFolderName(str);
                    if(result != 0){
                        continue;
                    }
                    Folder folder = new Folder();
                    folder.setFolderName(str);
                    folder.setParentFolder(next.getFolderId().toString());
                    Folder save = folderRepository.save(folder);
                } else {
                    String fileName;
                    if (str.contains("/")) {
                        fileName = str.substring(str.lastIndexOf("/") + 1);
                    } else {
                        fileName =str;
                    }
                    int result = fileRepository.findByFileName(fileName);
                    if(result != 0){
                        continue;
                    }
                    File file = new File();
                    file.setFileName(fileName);
                    file.setFileType(str.substring(str.lastIndexOf(".")));
                    file.setFolderId(next.getFolderId().toString());
                    fileRepository.save(file);
                }
            }
        }

        return data;
    }

    @GetMapping("/deleteAll")
    @Operation(summary = "删除数据库里的所有文件和文件夹")
    public String deleteAll() {
        folderRepository.deleteAll();
        fileRepository.deleteAll();
        return "数据删除成功";
    }

    public void save(String param, String folderId) throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        System.out.println(param); // 输出当前文件夹的名称
        Folder folder = new Folder();
        folder.setFolderName(param);
        folder.setParentFolder(folderId);
        Folder save = folderRepository.save(folder);
        List<String> list = webDavUtils.list(param);
        for (String str : list) {
            if (FileUtils.isDirectory(str)) {
                Folder folder2 = new Folder();
                folder2.setFolderName(str);
                folder2.setParentFolder(save.getFolderId().toString());
                Folder save2 = folderRepository.save(folder2);
                if (!("").equals(str)) {
                    save(url + save2.getEncodeFolderName(), save2.getFolderId().toString());
                }
            }
        }
    }
}
