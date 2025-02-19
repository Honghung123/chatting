package com.honghung.chatapp.model.media;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoUploadData extends FileUploadData {

    // @JsonProperty("asset_folder")
    // private String assetFolder;

    // private String signature;

    // @JsonProperty("created_at")
    // private Instant createdAt;
    // @JsonProperty("asset_id")
    // private String assetId;

    // private VideoMetadata video;

    // private String type;

    // @JsonProperty("public_id")
    // private String publicId;

    // private double duration;

    // @JsonProperty("bit_rate")
    // private int bitRate;

    // private int pages;

    // @JsonProperty("original_filename")
    // private String originalFilename;

    // private boolean placeholder;

    // @JsonProperty("playback_url")
    // private String playbackUrl;

    // private AudioMetadata audio;

    // @JsonProperty("nb_frames")
    // private int nbFrames;

    // private int height;

    // private int rotation;

    // private String format;

    // @JsonProperty("resource_type")
    // private String resourceType;

    // @JsonProperty("secure_url")
    // private String secureUrl;

    // @JsonProperty("version_id")
    // private String versionId;

    // @JsonProperty("display_name")
    // private String displayName;

    // private long version;

    // @JsonProperty("frame_rate")
    // private double frameRate;

    // private String url;

    // private String[] tags;

    // @JsonProperty("is_audio")
    // private boolean isAudio;

    // @JsonProperty("api_key")
    // private String apiKey;

    // private long bytes;

    // private int width;

    // private String etag;

    private VideoMetadata video;
    
    private double duration;
    
    @JsonProperty("bit_rate")
    private int bitRate;
    
    private int pages;
    
    @JsonProperty("playback_url")
    private String playbackUrl;
    
    private AudioMetadata audio;
    
    @JsonProperty("nb_frames")
    private int nbFrames;
    
    private int rotation;
    
    @JsonProperty("frame_rate")
    private double frameRate;
    
    @JsonProperty("is_audio")
    private boolean isAudio;

    @Data
    public static class VideoMetadata {
        private String codec;
        @JsonProperty("bit_rate")
        private int bitRate;
        private String dar;
        @JsonProperty("time_base")
        private String timeBase;
        private int level;
        @JsonProperty("pix_format")
        private String pixFormat;
        private String profile;
    }

    @Data
    public static class AudioMetadata {
        private String codec;
        @JsonProperty("bit_rate")
        private int bitRate;
        private int channels;
        @JsonProperty("channel_layout")
        private String channelLayout;
        private int frequency;
    }

    public static final VideoUploadData convertFromMapObject(Map<String, Object> map) {
        // System.out.println(map.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Để hỗ trợ các kiểu như ZonedDateTime
        return objectMapper.convertValue(map, VideoUploadData.class);
    }
}
