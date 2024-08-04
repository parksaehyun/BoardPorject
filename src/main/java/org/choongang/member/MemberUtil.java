package org.choongang.member;

import org.choongang.member.constants.Authority;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberUtil {

    // 로그인 상태 체크
    public boolean isLogin() {
        return getMember() != null; // null이 아니면 로그인 상태이다 // getMember : 엔티티
    }

    // 권한 체크
    public boolean isAdmin() {
        if (isLogin()) {
            Member member = getMember();
            List<Authorities> authorities = member.getAuthorities();
            return authorities.stream().anyMatch(s -> s.getAuthority() == Authority.ADMIN); // anyMatch: 값중에 한개라도 매칭되는 것이 있는 가 // 권한이 더 있으면 Authority.ADMIN &&(or)로 더 추가해서 쳌해보면 됨
        }

        return false;
    }

    // 로그인한 회원 정보 가져오기
    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo) {
            // authentication : 로그인한 회원정보 , authentication.isAuthenticated() : 로그인한 상태인지, authentication.getPrincipal() instanceof MemberInfo : 문자열(어나니머스)가 아닌 UserDetails의 구현객체인가
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();

            return memberInfo.getMember(); // MemberInfo의 private Member member;에 대한 get메서드 = 엔티티 Member
        }
        return null;
    }
}
