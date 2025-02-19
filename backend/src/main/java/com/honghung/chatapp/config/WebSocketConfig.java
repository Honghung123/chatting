package com.honghung.chatapp.config;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.honghung.chatapp.utils.JSONUtils;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("*").withSockJS()   ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) { 
        channelRegistration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel messageChannel) { 
                System.out.println(JSONUtils.convertToJSON(message));
                // messageChannel.send(message);
                return message;
            }   
        });
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> list) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        list.add(converter);
        return true;
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2,
                    Exception arg3) {
                System.out.println("After Handshake");
            }

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                    WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        System.out.println("===================================");
                if (request instanceof ServerHttpRequest) {
                    ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
                    HttpSession session = serverHttpRequest.getServletRequest().getSession();
                    System.out.println(session.getId());
                    attributes.put("sessionId", session.getId());
                }
                System.out.println("===================================");
                return true;
            }
        };
    }
}
