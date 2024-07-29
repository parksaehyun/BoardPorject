package org.choongang.global.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.build();
    }

    @Bean // 비밀번호 해시화(비크립트) -> 스프링 시큐리티가 제공하는 기능
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
