package com.honghung.chatapp.service.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.honghung.chatapp.dto.request.chat.MutipleFileUploadRequest;
import com.honghung.chatapp.model.media.FileUploadData;
import com.honghung.chatapp.model.media.ImageUploadData;
import com.honghung.chatapp.model.media.VideoUploadData;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CloudImageFileUploadService implements MediaFileUploadService {
    private final Cloudinary cloudinary;
    @Override
    public FileUploadData uploadFile(MultipartFile file, String type) {
        return switch (type) {
            case "IMAGE" -> uploadImageFile(file);
            case  "AUDIO", "VIDEO" -> uploadVideoFile(file);
            case "FILE" -> uploadAnotherFile(file);
            // case "FILE" -> null;
            default -> null;
        } ;
    }
    
    @Override
    public ImageUploadData uploadImageFile(MultipartFile file) {
        try {
            Map params = ObjectUtils.asMap(
                "resource_type", "image",
                "display_name", file.getOriginalFilename()
            );
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return ImageUploadData.convertFromMapObject(uploadResult);
            // return (String) uploadResult.get("url");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public VideoUploadData uploadVideoFile(MultipartFile file) {
        try {
            Map params = ObjectUtils.asMap(
                "resource_type", "video", 
                "chunk_size", 6000000, 
                "display_name", file.getOriginalFilename());
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return VideoUploadData.convertFromMapObject(uploadResult); 
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public FileUploadData uploadAnotherFile(MultipartFile file) {
        try {
            Map params = ObjectUtils.asMap(
                "resource_type", "raw",
                "display_name", file.getOriginalFilename());
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return FileUploadData.convertFromMapObject(uploadResult); 
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public void deleteFile(String fileId) {
        try {
            cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to destroy file", e);
        }
    }

    @Override
    public List<FileUploadData> uploadMultipleFiles(MultipartFile[] files, String[] types) { 
        List<FileUploadData> fileDetailsList = new ArrayList<>();
        for(int i = 0; i < files.length; i++) { 
            fileDetailsList.add(this.uploadFile(files[i], types[i]));
        }
        return fileDetailsList;
    }
    
}
