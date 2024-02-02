package com.jfudali.coursesapp.file.controller;

import java.io.IOException;

import com.jfudali.coursesapp.exceptions.FileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jfudali.coursesapp.file.dto.UploadFileResponse;
import com.jfudali.coursesapp.file.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestPart(value = "video") MultipartFile video)
            throws IOException, FileException {
        return new ResponseEntity<UploadFileResponse>(new UploadFileResponse(this.fileService.uploadFile(video)),
                HttpStatus.OK);
    }
}
