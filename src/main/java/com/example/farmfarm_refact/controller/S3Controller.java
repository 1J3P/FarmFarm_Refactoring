package com.example.farmfarm_refact.controller;
import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.converter.FileConverter;
import com.example.farmfarm_refact.dto.FileResponseDto;
import com.example.farmfarm_refact.entity.FileEntity;
import com.example.farmfarm_refact.service.FileService;
import com.example.farmfarm_refact.service.S3Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.S3_FAIL_UPLOAD_FILE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
    @Autowired
    static S3Service S3Service;
    @Autowired
    static FileService fileService;
    @PostMapping("/file")
    public ApiResponse<List<FileResponseDto.FileCreateResponseDto>> uploadFile(@RequestPart List<MultipartFile> multipartFiles) {
        System.out.println("file 요청");
        List<FileEntity> filesList = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFiles) {
                String filename = S3Service.uploadFile(file);
                String url = S3Service.getUrl(filename);
                FileEntity files = new FileEntity(filename, url);
                FileEntity newfile = fileService.save(files);
                filesList.add(newfile);
            }
            return ApiResponse.onSuccess(FileConverter.toFileCreateResponseDtoList(filesList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionHandler(S3_FAIL_UPLOAD_FILE);
        }
    }
    @DeleteMapping("/file/{fileIdx}")
    public static ResponseEntity<String> deleteFile(@PathVariable("fileIdx") int fileIdx) {
        try {
            FileEntity file = fileService.findByFileId(fileIdx);
            S3Service.deleteFile(file.getFilename());
            return ResponseEntity.ok().body("삭제 처리가 완료되었습니다.");
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/file")
    public ResponseEntity<String> getFile(@RequestParam String fileName) {

        String url = S3Service.getUrl(fileName).toString();

        return ResponseEntity.ok().body(url);
    }
}

