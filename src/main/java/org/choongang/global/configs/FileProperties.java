package org.choongang.global.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix="file.upload") // 앞에 붙는 부분 고정
public class FileProperties {
    private String path; // file.upload.path
    private String url; // file.upload.url
}
