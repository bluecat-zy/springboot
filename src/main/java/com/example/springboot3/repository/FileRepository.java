package com.example.springboot3.repository;

import com.example.springboot3.entity.File;
import com.example.springboot3.entity.Folder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File,String> {
    @Query("select count(1) from File f where f.fileName = :fileName")
    int findByFileName(@Param("fileName") String fileName);
}
