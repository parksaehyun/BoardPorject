package org.choongang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.member.controllers.RequestLogin;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    // 로그인 실패시에 유입되는 메서드
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        HttpSession session = request.getSession(); // 세션 가져오기

        RequestLogin form = new RequestLogin(); // 커맨드객체 생성
        form.setEmail(request.getParameter("email")); //  커맨드객체에 사용자가 입력한 아이디 값 담아줌
        form.setPassword(request.getParameter("password")); //  커맨드객체에 사용자가 입력한 비번 값 담아줌
        // 커맨드 객체에 값을 담아줌으로서 거맨드객체 검증 @Valid 수행 가능하게 됨

        // 예외
        if (exception instanceof BadCredentialsException) { // 아이디 또는 비밀번호가 일치하지 않는 경우
            form.setCode("BadCredentials.Login"); // 커맨드 객체에 에러상황 별 에러코드 담아줌
        } else if (exception instanceof DisabledException) { // 탈퇴한 회원
            form.setCode("Disabled.Login");
        } else if (exception instanceof CredentialsExpiredException) { // 비밀번호 유효기간 만료
            form.setCode("CredentialsExpired.Login");
        } else if (exception instanceof AccountExpiredException) { // 사용자 계정 유효기간 만료 // isAccountNonExpired()이게 false이면 발생
            form.setCode("AccountExpired.Login");
        } else if (exception instanceof LockedException) { // 사용자 계정이 잠겨있는 경우 // isAccountNonExpired() 이게 false이면 발생
            form.setCode("Locked.Login");
        } else {
            form.setCode("Fail.Login");
        }

        form.setDefaultMessage(exception.getMessage()); // 기본 메세지 // 메세지가 없을 때 이거 출력

        System.out.println(exception);

        form.setSuccess(false); // 로그인 실패상태라는 것 알려주기
        session.setAttribute("requestLogin", form); // 세션에 커맨드객체 값(아이디, 비번, 에러코드) 담아 줌 // 뷰에서 이 값을 바탕으로 에러 메세지 출력

        // 로그인 실패시 로그인 페이지 이동(찐 사이트 이동)
        response.sendRedirect(request.getContextPath() + "/member/login");
    }
}