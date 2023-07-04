package com.example.springboot3.repository;

import com.example.springboot3.entity.Folder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends CrudRepository<Folder,String> {
    @Query("select count(1) from Folder f where f.folderName = :folderName")
    int findByFolderName(@Param("folderName") String folderName);
}
