package com.neo.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.neo.event.EventDemoPublish;
import com.neo.minio.MinIOUtils;

@RestController
public class HelloController {

    @Resource
    EventDemoPublish eventDemoPublish;

    @RequestMapping("/")
    public String index(@RequestParam(value = "id", required = false, defaultValue = "default") String id) {
        eventDemoPublish.publish(id);
        return "Hello!";
    }

    @PostMapping("/upload")
    //    @ApiOperation(value = "上传文件，返回文件信息")
    public ResponseEntity<String> uploadFile(MultipartFile file) {

        MinIOUtils.uploadFile("picture", file.getName(), file);

        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getFile(
            @RequestParam(value = "bucket", required = false, defaultValue = "picture") String bucket,
            @RequestParam(value = "name") String name) {
        return ResponseEntity.ok(MinIOUtils.getFileUrl(bucket, name));
    }

    @GetMapping("download")
    //    @ApiOperation(value = "下载文件，返回文件流，需要文件的id")
    public ResponseEntity<org.springframework.core.io.Resource> download(
            @RequestParam(value = "bucket", required = false, defaultValue = "picture") String bucket,
            @RequestParam(value = "name") String name) {
        return MinIOUtils.downloadFile(bucket, name);
    }
}