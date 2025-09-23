package com.fsd.sdp.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:8084", // Frontend local dev
                    "http://frontend:80",    // Docker frontend container
                    "https://reactfrontend-orcin.vercel.app" // Production frontend
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/ws/**")
                .allowedOrigins(
                    "http://localhost:8084", // Frontend local dev
                    "http://frontend:80",    // Docker frontend container
                    "https://reactfrontend-orcin.vercel.app" // Production frontend
                )
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
