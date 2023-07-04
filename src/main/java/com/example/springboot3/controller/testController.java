package com.example.springboot3.controller;


import com.example.springboot3.common.Result;
import com.example.springboot3.entity.Folder;
import com.example.springboot3.exception.ConnectException;
import com.example.springboot3.exception.SystemException;
import com.example.springboot3.repository.FolderRepository;
import com.example.springboot3.repository.UserRepository;
import com.example.springboot3.entity.User;
import com.example.springboot3.retry.RetryPolicy;
import com.example.springboot3.webdav.WebDavUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController("/test")
@Tag(name = "测试controller", description = "controller描述")
public class testController {

    @Value("${webdav.url}")
    public String url;
    @Autowired
    public WebDavUtils webDavUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    CriteriaBuilder criteriaBuilder;

    @GetMapping("/test")
    @Operation(summary = "测试接口")
    public String test(@Parameter(name = "参数", description = "参数描述") Integer param) {
        Optional<User> user = userRepository.findById(param);
        if (user.isPresent()) {
            return user.get().getName();
        }
        System.out.println(folderRepository.findById("1").get());
        return user.toString();
    }

    @GetMapping("/folder")
    @Operation(summary = "文件夹测试接口")
    public String folder(@Parameter(name = "参数", description = "文件夹id") String param) {

        Optional<Folder> folder = folderRepository.findById(param);
        if (folder.isPresent()) {
            return folder.get().getFolderName();
        }
        return folder.toString();
    }

    @GetMapping("/criteria")
    @Operation(summary = "文件夹测试接口")
    public String criteria(@Parameter(name = "参数", description = "文件夹id") String param) {

        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        return "";
    }

    @GetMapping("/retry")
    @Operation(summary = "测试接口")
    public Result retry(@Parameter(name = "参数", description = "参数描述") Integer param) {
        AtomicReference<List<String>> list = new AtomicReference<>();
        try {
            Optional<Folder> folder = folderRepository.findById(String.valueOf(param.intValue()));
            if (folder.isPresent()) {
                RetryPolicy<Object> retryPolicy = new RetryPolicy<Exception>().create()
                        .withMaxRetry(3)
                        .withDelay(1000)
                        .withRetryCondition(e -> e instanceof ConnectException);
                retryPolicy.execute(() -> {
                    list.set(webDavUtils.list(url + folder.get().getFolderName()));
                });
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success("测试成功", list.get());
    }

}
