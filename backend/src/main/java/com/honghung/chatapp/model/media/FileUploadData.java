package com.honghung.chatapp.model.media;

import java.time.ZonedDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honghung.chatapp.utils.JSONUtils;

import lombok.Data; 

@Data 
public class FileUploadData {
    @JsonProperty("asset_folder")
    private String assetFolder;

    private String signature;
    
    private String format;
    
    @JsonProperty("resource_type")
    private String resourceType;
    
    @JsonProperty("secure_url")
    private String secureUrl;
    
    @JsonProperty("asset_id")
    private String assetId;
    
    @JsonProperty("version_id")
    private String versionId;
    
    private String type;
    
    @JsonProperty("display_name")
    private String displayName;
    
    private long version;
    
    private String url;
    
    @JsonProperty("public_id")
    private String publicId; 
    
    @JsonProperty("original_filename")
    private String originalFilename;
    
    @JsonProperty("api_key")
    private String apiKey;
    
    private int bytes;
    
    private int width;
    
    private String etag;
    
    private boolean placeholder;
    
    private int height;

    private String[] tags;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    public static FileUploadData convertFromMapObject(Map<String, Object> uploadResult) { 
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Để hỗ trợ các kiểu như ZonedDateTime
        System.out.println(JSONUtils.convertToJSON(uploadResult));
        return objectMapper.convertValue(uploadResult, FileUploadData.class);
    }
}
