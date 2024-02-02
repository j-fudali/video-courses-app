package com.jfudali.coursesapp.file.service;

import java.io.IOException;
import java.util.Date;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.jfudali.coursesapp.exceptions.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile) throws IllegalStateException, IOException, FileException {
        if(!isVideo(multipartFile)) throw new FileException("File is not a video");
        String objKeyName = +new Date().getTime() + multipartFile.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        String key = "videos/" + objKeyName;
        amazonS3.putObject(new PutObjectRequest(bucket, key,
                multipartFile.getInputStream(), objectMetadata));
        return key;
    }
    public void deleteFile(String fileUrl){
        //TODO
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileUrl));
    }
    private boolean isVideo(MultipartFile video){
        return (video.getContentType() != null && video.getContentType().startsWith("video"));
    }
}
