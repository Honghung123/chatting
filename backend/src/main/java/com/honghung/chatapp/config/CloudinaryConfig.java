package com.honghung.chatapp.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloudinaryUrl}") String cloudinaryUrl;
    
    public static final Map params2 = ObjectUtils.asMap(
        "quality_analysis", true
);

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary( cloudinaryUrl );
    }
}
