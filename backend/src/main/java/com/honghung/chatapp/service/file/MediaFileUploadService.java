package com.honghung.chatapp.service.file;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.honghung.chatapp.model.media.FileUploadData;
import com.honghung.chatapp.model.media.ImageUploadData;
import com.honghung.chatapp.model.media.VideoUploadData;

public interface MediaFileUploadService {
    FileUploadData uploadFile(MultipartFile file, String type);
    List<FileUploadData> uploadMultipleFiles(MultipartFile[] files, String[] types);
    ImageUploadData uploadImageFile(MultipartFile file);
    FileUploadData uploadAnotherFile(MultipartFile file);
    VideoUploadData uploadVideoFile(MultipartFile file);
    void deleteFile(String fileId);
}
