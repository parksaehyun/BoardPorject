package org.choongang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    // 로그인 성공시에 유입되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Authentication authentication : 로그인한 회원의 정보가 담겨있는 곳

        HttpSession session = request.getSession(); // 세션 가져오기

        // 세션에 남아 있는 requestLogin 값 제거
        session.removeAttribute("requestLogin");
        // 로그인이 성공했으면 세션에 담겨있는 아이디, 비번 값 필요 없으니까 세션 제거
        // but 완전히 값이 제거되는 건 아님 로그인 하다가 나 안할래 하고 나가버리는 경우 여기로 유입이 안되니 세션 그대로 유지됨
        // 참고) 세션에 값을 유지한 이유는 로그인의 경우 spring security가 로그인 처리를 해주어서 PostMapping을 안 씀
        //      -> 그럼 검증을 어떻게 함? 보통 검증은 PostMappig에서 @Valid 통해서 하곤 했는데
        //      -> 그래서 커맨드객체 검증 및 글로벌 에러 검증을 위해 커맨드객체(request범위)보다 더 넓은 범위인 session 에 값을 담아 줌 = model에도 자동으로 값이 담김


        // 로그인 성공시 - redirectUrl이 있으면 해당 주소로 이동, 아니면 메인 페이지 이동
        String redirectUrl = request.getParameter("redirectUrl");
        redirectUrl = StringUtils.hasText(redirectUrl) ? redirectUrl.trim() : "/";

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}