package com.alala.checkpointbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 啟用 CORS，並指定使用我們自訂的 corsConfigurationSource
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. 通常 API 服務會停用 CSRF
            .csrf(AbstractHttpConfigurer::disable)
            
            // 3. 設定請求授權規則
            .authorizeHttpRequests(auth -> auth
                // 3.1 關鍵！明確允許所有的 OPTIONS 預檢請求
                .requestMatchers(OPTIONS, "/**").permitAll()
                .requestMatchers(GET, "/**").permitAll()
                .requestMatchers(PUT, "/**").permitAll()
                .requestMatchers(DELETE, "/**").permitAll()
                .requestMatchers(POST, "/**").permitAll()

                // 3.2 允許登入相關的 API 不需認證即可訪問
//                .requestMatchers("/api/user/mailLogin", "/api/user/login").permitAll()
                
                // 3.3 其他所有請求都需要認證
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ！！！設定您 GCS 前端的來源！！！
        configuration.setAllowedOrigins(List.of("https://storage.googleapis.com"));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 針對所有路徑套用此 CORS 設定
        source.registerCorsConfiguration("/**", configuration); 
        
        return source;
    }
}