package io.meimberg.licenses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${app.cors.allowed-origins:http://localhost:3000,https://licenses.meimberg.io}")
  private String allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] origins = allowedOrigins.split(",");
    String[] patterns = new String[origins.length + 2];
    patterns[0] = "http://localhost:*";
    patterns[1] = "http://127.0.0.1:*";
    System.arraycopy(origins, 0, patterns, 2, origins.length);
    
    registry.addMapping("/api/**")
        .allowedOriginPatterns(patterns)
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
