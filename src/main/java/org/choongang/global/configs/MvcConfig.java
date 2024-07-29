package org.choongang.global.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing // 이벤트리스너관련 설정추가 애노테이션 // 불필요한 자원소비 방지위한 애노테이션
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * <input type="hidden" name="_method" value="PATCH">  -> PATCH 방식으로 요청
     * ?_method=DELETE
     * @return
     */

    @Bean
    public HiddenHttpMethodFilter httpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
