package com.fsd.sdp.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // API endpoints
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:8084",                 // local frontend dev
                    "http://frontend:80",                    // docker frontend container
                    "https://reactfrontend-orcin.vercel.app" // production
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        // WebSocket endpoints
        registry.addMapping("/ws/**")
                .allowedOrigins(
                    "http://localhost:8084",
                    "http://frontend:80",
                    "https://reactfrontend-orcin.vercel.app"
                )
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
