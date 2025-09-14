package com.alala.checkpointbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許所有路徑
                .allowedOrigins("https://storage.googleapis.com/checkpoint_frontend/index.html") // 允許的來源
                .allowedOrigins("https://storage.googleapis.com/checkpoint_frontend/") // 允許的來源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*"); // 允許所有 Header
    }
}
