package com.honghung.chatapp.model.media;
 
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
 
import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
public class ImageUploadData extends FileUploadData {
    // @JsonProperty("asset_folder")
    // private String assetFolder;
    // private String signature;
    // private String format;
    // @JsonProperty("resource_type")
    // private String resourceType;
    // @JsonProperty("secure_url")
    // private String secureUrl;
    // @JsonProperty("created_at")
    // private ZonedDateTime createdAt;
    // @JsonProperty("asset_id")
    // private String assetId;
    // @JsonProperty("version_id")
    // private String versionId;
    // private String type;
    // @JsonProperty("display_name")
    // private String displayName;
    // private long version;
    // private String url;
    // @JsonProperty("public_id")
    // private String publicId;
    // private List<String> tags;
    // @JsonProperty("original_filename")
    // private String originalFilename;
    // @JsonProperty("api_key")
    // private String apiKey;
    // private int bytes;
    // private int width;
    // private String etag;
    // private boolean placeholder;
    // private int height; 

    public static final ImageUploadData convertFromMapObject(Map<String, Object> map) {
        // System.out.println(map.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Để hỗ trợ các kiểu như ZonedDateTime
        return objectMapper.convertValue(map, ImageUploadData.class);
    }
}

