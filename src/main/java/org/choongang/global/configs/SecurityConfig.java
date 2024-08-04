package org.choongang.global.configs;

import org.choongang.member.services.LoginFailureHandler;
import org.choongang.member.services.LoginSuccessHandler;
import org.choongang.member.services.MemberAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 웹에 관련된 기본적인 보안 활성화 ex) 요청을 무한대로 보내면 접근 차단...
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* 로그인, 로그아웃 S */
        http.formLogin(f -> {
            f.loginPage("/member/login") // login url이 무엇인지 알려주기
                .usernameParameter("email") // form에서 name이 뭔지 알려주기
                .passwordParameter("password") // form에서 name이 뭔지 알려주기
                //. successForwardUrl("/")// 성공시 이동할 경로 -> 이렇게는 잘 안함
                //.failureUrl("/member/login?error=true"); // 실패 시 이동할 주소 -> 이렇게는 잘 안함
                    .successHandler(new LoginSuccessHandler()) // 성공시 이동할 경로
                    .failureHandler(new LoginFailureHandler()); // 실패 시 이동할 주소
        });

        http.logout(f -> {
            f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // logout url이 무엇인지
                .logoutSuccessUrl("/member/login"); // logout 후 이동할 경로
        });
        /* 로그인, 로그아웃 E */

        /* 인가(접근 통제) 설정 S */
        http.authorizeHttpRequests(c -> {
            /*
            c.requestMatchers("/member/**").anonymous()
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .anyRequest().authenticated();
            */
            c.requestMatchers("/mypage/**").authenticated() // 회원 전용
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .anyRequest().permitAll(); // anyRequest() : 모든페이지, permitAll() : 다 허용


        });

        http.exceptionHandling(c -> {
            c.authenticationEntryPoint(new MemberAuthenticationEntryPoint()).accessDeniedHandler((req, res, e) -> {
                res.sendError(HttpStatus.UNAUTHORIZED.value());
            });
        });

        /* 인가(접근 통제) 설정 E */

        // iframe 자원 출처를 같은 서버 자원으로 한정
        http.headers(c -> c.frameOptions(f -> f.sameOrigin()));

        return http.build();
    }

    @Bean // 비밀번호 해시화(비크립트) -> 스프링 시큐리티가 제공하는 기능
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
