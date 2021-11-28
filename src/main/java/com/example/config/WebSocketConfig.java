package com.example.config;

import com.example.bean.MacroHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration // 表明是配置类(必须要有)
@EnableWebSocket // 启用WebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(macroHandler(), "/macro"); // 将MacroHandler映射到"/macro"
    }

    @Bean
    public MacroHandler macroHandler() { // 声明MacroHandler bean
        return new MacroHandler();
    }
}