package com.honghung.chatapp.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.honghung.chatapp.constant.KafkaTopic;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); 
        return new DefaultKafkaProducerFactory<>(configProps);
    }

     @Bean
    KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // Declare topics for producers
    @Bean
    NewTopic confirmAccountLinkTopic() {
        return new NewTopic(KafkaTopic.SEND_CONFIRM_ACCOUNT_LINK_VIA_EMAIL, 3, (short) 1);
    }

    @Bean
    NewTopic printAndWriteLogTopic() {
        return new NewTopic(KafkaTopic.PRINT_AND_WRITE_LOG, 3, (short) 1);
    }

    @Bean
    NewTopic updateUserOnlineStatusOnCache(){
        return new NewTopic(KafkaTopic.UPDATE_USER_ONLINE_STATUS_ON_CACHE, 3, (short) 1);
    }
    
    @Bean
    NewTopic sendPostNotificationTopic(){
        return new NewTopic(KafkaTopic.SEND_POST_NOTIFICATION_TO_USER, 3, (short) 1);
    }
}
