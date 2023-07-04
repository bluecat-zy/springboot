package com.example.springboot3.entity;

import jakarta.persistence.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "folder_id")
    private Integer folderId;
    @Basic
    @Column(name = "folder_name")
    private String folderName;
    @Basic
    @Column(name = "parent_folder")
    private String parentFolder;
    @Basic
    @Column(name = "folder_path")
    private String folderPath;

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getEncodeFolderName() throws UnsupportedEncodingException {
        return URLEncoder.encode(folderName, "UTF-8");
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

}
