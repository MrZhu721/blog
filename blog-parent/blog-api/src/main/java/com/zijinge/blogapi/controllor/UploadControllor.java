package com.zijinge.blogapi.controllor;

import com.zijinge.blogapi.Vo.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/upload")
public class UploadControllor {

    public Result upload(@RequestParam("image") MultipartFile file) {

        String originalFilename = file.getOriginalFilename();   // 获取原文件名称 如 aa,png
        String uuid = UUID.randomUUID().toString();     // 自动生成唯一标识前缀
        // 唯一的文件名称
        String fileName = uuid + "." + StringUtils.substringAfterLast(originalFilename, ".");
        // 上传文件 ，到哪？？

        return null;
    }
}