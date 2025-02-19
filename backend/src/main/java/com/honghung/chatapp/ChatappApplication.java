package com.honghung.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.honghung.chatapp.constant.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		AppProperties.class
})
@EnableCaching
public class ChatappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatappApplication.class, args);
	}

}
