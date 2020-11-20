package com.neo.minio;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-11-20 14:09
 **/
@Slf4j
public class MinIOUtils {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://127.0.0.1:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();


    public static void uploadFile(String bucket, String fileName, MultipartFile file) {
        try {
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
            if (!isExist) {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
            }

            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                    .contentType(file.getContentType())
                    .bucket(bucket)
                    .object(StringUtils.isBlank(fileName) ? file.getOriginalFilename() : file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
            log.info("uploadFile response:{},{}", objectWriteResponse.object(), objectWriteResponse.etag());
            //            minioClient.uploadObject(
            //                    UploadObjectArgs.builder()
            //                            .bucket(bucket)
            //                            .contentType(file.getContentType())
            //                            .object()
            //                            .filename(fileName)
            //                            .build());
        } catch (Exception e) {
            log.error("uploadFile error,bucket:{},fileName:{}", bucket, fileName, e);
        }

    }

    public static String getFileUrl(String bucket, String fileName) {
        try {

            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .method(Method.GET)
                    .object(fileName)
                    .build());


        } catch (Exception e) {
            log.error("downloadFile error,bucket:{},fileName:{}", bucket, fileName, e);
        }
        return null;
    }

    public static ResponseEntity<Resource> downloadFile(String bucket, String fileName) {

        //        byte[] bytes = null;
        //        byte[] bytes = IOUtils.toByteArray(response.);
        //        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {

            StatObjectArgs statObject = StatObjectArgs.builder().bucket(bucket)
                    .object(fileName)
                    .build();

            ObjectStat objectStat = minioClient.statObject(statObject);


            InputStream in = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build());
            //            bytes = new byte[object.read()];
            //            log.info("downloadFile response:{},{}", object.bucket(), object.region());
            //            int read = object.read(bytes);
            //            log.info("downloadFile read:{},{}", read);
            //            object.close();
            //            return new ByteArrayResource(bytes);
            return ResponseEntity.ok().contentType(MediaType.valueOf(objectStat.contentType()))
                    .body(new InputStreamResource(in));
        } catch (ErrorResponseException ee) {

        } catch (Exception e) {
            log.error("downloadFile error,bucket:{},fileName:{}", bucket, fileName, e);
        }
        ClassPathResource classPathResource = new ClassPathResource("404.jpg");
        try {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(classPathResource.getInputStream()));
        } catch (IOException e) {
            log.error("get default file error,bucket:{},fileName:{}", bucket, fileName, e);

        }
        return null;
    }
}
