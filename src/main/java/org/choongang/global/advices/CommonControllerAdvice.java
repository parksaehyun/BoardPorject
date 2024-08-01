package org.choongang.global.advices;

import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice("org.choongang") // 적용범위 설정 : org.choongang을 포함한 하위 패키지
public class CommonControllerAdvice {
    // 모든 컨트롤러에 공통으로 유지될 값 정의(컨트롤러 실행 전)

    private final MemberUtil memberUtil;

    @ModelAttribute("loggedMember") // 회원정보 가져오기
    public Member loggefMember() {
        return memberUtil.getMember();
    }

    @ModelAttribute("isLogin")
    public boolean isLogin() {
        return memberUtil.isLogin();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return memberUtil.isAdmin();
    }
}
