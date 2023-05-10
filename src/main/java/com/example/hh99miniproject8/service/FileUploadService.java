//package com.example.hh99miniproject8.service;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//@NoArgsConstructor
//public class FileUploadService {
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    private AmazonS3Client amazonS3Client;
//
//    public String upload(MultipartFile multipartFile) throws IOException {
//        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
//
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentLength(multipartFile.getInputStream().available());
//
//        amazonS3Client.putObject(bucket, s3FileName, multipartFile.getInputStream(), objectMetadata);
//        return amazonS3Client.getUrl(bucket, s3FileName).toString();
//    }
//
//}
