package com.honghung.chatapp.controller;   
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.honghung.chatapp.dto.request.chat.FileUploadRequest;
import com.honghung.chatapp.dto.request.chat.MutipleFileUploadRequest;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.file.FileDetailsResponse;
import com.honghung.chatapp.mapper.FileStorageMapper;
import com.honghung.chatapp.model.media.FileUploadData;
import com.honghung.chatapp.model.media.ImageUploadData;
import com.honghung.chatapp.service.file.CloudImageFileUploadService; 

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {
    private final CloudImageFileUploadService fileUploadService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponseEntity<List<FileDetailsResponse>> uploadFile(@RequestParam() MultipartFile[] files, String[] types) { 
        List<FileUploadData> fileImageUrls = fileUploadService.uploadMultipleFiles(files, types);
        List<FileDetailsResponse> fileDetailsResponses = fileImageUrls.stream().map(FileStorageMapper::convertFromFileDetails).toList();
        return SuccessResponseEntity.from(HttpStatus.OK, "Upload file successfully", fileDetailsResponses);
    }

    @DeleteMapping("/destroy/{fileId}")
    public SuccessResponseEntity<String> deleteFile(@PathVariable("fileId") String fileId) {
        fileUploadService.deleteFile(fileId);
        return SuccessResponseEntity.from(HttpStatus.NO_CONTENT, "Delete file successfully", fileId);
    }

    // @GetMapping("/get-file/{fileName:.+}")
    // public ResponseEntity<byte[]> getAUploadedFile(@PathVariable("fileName") String fileName) {
    //     byte[] fileContent = fileUploadService.readFileContent(fileName);
    //     return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileContent);
    // }

    // @GetMapping("/get-all")
    // public SuccessResponseEntity<List<String>> getUploadedFiles() {
    //     var uploadedFiles = fileUploadService.loadAllFiles();
    //     List<String> uploadedFileUrls =
    //             uploadedFiles
    //                     .map(
    //                             path -> {
    //                                 var mvcBuilder =
    //                                         MvcUriComponentsBuilder.fromMethodName(
    //                                                         FileUploadController.class,
    //                                                         "getAUploadedFile",
    //                                                         path.getFileName().toString())
    //                                                 .build();
    //                                 return mvcBuilder.toUri().toString();
    //                             })
    //                     .toList();
    //     return SuccessResponseEntity.from(
    //             HttpStatus.OK, "Get uploaded file list successfully", uploadedFileUrls);
    // }
}
