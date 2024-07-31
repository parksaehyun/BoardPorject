package org.choongang.global.configs;

import org.choongang.member.services.LoginFailureHandler;
import org.choongang.member.services.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* 로그인, 로그아웃 S */
        http.formLogin(f -> {
            f.loginPage("/member/login")
                .usernameParameter("email") // form에서 name이 뭔지 알려주기
                .passwordParameter("password") // form에서 name이 뭔지 알려주기
                //. successForwardUrl("/")// 성공시 이동할 경로 -> 이렇게는 잘 안함
                //.failureUrl("/member/login?error=true"); // 실패 시 이동할 주소 -> 이렇게는 잘 안함
                    .successHandler(new LoginSuccessHandler()) // 성공시 이동할 경로
                    .failureHandler(new LoginFailureHandler()); // 실패 시 이동할 주소
        });

        http.logout(f -> {
            f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/member/login");
        });
        /* 로그인, 로그아웃 E */

        /* 인가(접근 통제) 설정 S */
        http.authorizeRequests(c -> {
            c.requestMatchers("/mapage/**").authenticated() // 회원 전용
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .anyRequest().permitAll();
        });
        /* 인가(접근 통제) 설정 E */

        return http.build();
    }

    @Bean // 비밀번호 해시화(비크립트) -> 스프링 시큐리티가 제공하는 기능
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
