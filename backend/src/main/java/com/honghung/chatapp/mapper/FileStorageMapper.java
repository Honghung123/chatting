package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.file.FileDetailsResponse;
import com.honghung.chatapp.model.media.FileUploadData; 

public final class FileStorageMapper {
    public static final FileDetailsResponse convertFromFileDetails(FileUploadData fileDetails){
        return FileDetailsResponse.builder()
        .publicId(fileDetails.getPublicId())
        .assetId(fileDetails.getAssetId())
        .versionId(fileDetails.getVersionId())
        .width(fileDetails.getWidth())
        .height(fileDetails.getHeight())
        .format(fileDetails.getFormat())
        .url(fileDetails.getUrl())
        .secureUrl(fileDetails.getSecureUrl())
        .bytes(fileDetails.getBytes())
        .createdAt(fileDetails.getCreatedAt())
        .displayName(fileDetails.getDisplayName())
        .build();
    }
}
