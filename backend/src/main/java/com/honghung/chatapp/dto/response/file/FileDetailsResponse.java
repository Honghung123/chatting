package com.honghung.chatapp.dto.response.file;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDetailsResponse {
    private String assetId;
    private String publicId;
    private String versionId;
    private int width;
    private int height;
    private String format;
    private String url;
    private String secureUrl;
    private ZonedDateTime createdAt;
    private String displayName;
    private int bytes; 
}
