package com.honghung.chatapp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.honghung.chatapp.component.message.Receiver;
import com.honghung.chatapp.constant.AppProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final AppProperties appProperties;
    @Bean
    Queue queue() {
        return new Queue(appProperties.getRabbitMQ().getQueueName(), false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(appProperties.getRabbitMQ().getTopicExchangeName());
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(appProperties.getRabbitMQ().getRoutingKey());
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(appProperties.getRabbitMQ().getQueueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    // Dang ky bean nay de tu dong nhan message va goi ham receiveMessage cua Receiver
    // Con nhan thu cong thi dung @RabbitListener(queues = "chatapp_queue")   
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
