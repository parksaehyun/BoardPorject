package org.choongang.global.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 파일의 정적경로 설정
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileConfig implements WebMvcConfigurer {

    private final FileProperties properties;

    /*
    @Value("${file.upload.path}")
    private String path;

    @Value("${file.upload.url}")
    private String url;
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(properties.getUrl() + "**")
                .addResourceLocations("file:///" + properties.getPath());
    }
}
