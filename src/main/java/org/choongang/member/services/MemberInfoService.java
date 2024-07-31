package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberInfo;
import org.choongang.member.constants.Authority;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // UsernameNotFoundException 회원정보 없을 때 던져지는 에러

        // 유저네임 = 이메일
        // 회원정보 조회하고 없을 시에는 스프링시큐리티가 이미 정해놓은 예외(UsernameNotFoundException)를 던져주면 됨
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        List<Authorities> temp = Objects.requireNonNullElse(member.getAuthorities(), List.of(Authorities.builder().member(member).authority(Authority.USER).build()));
        // Objects.requireNonNullElse()의 2번째 매개변수 : null일때의 기본값
        // 없을 때는 일반 사용자로 권한 넣어줌

        List<SimpleGrantedAuthority> authorities = temp.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority().name())).toList();
                // a = authority
        // SimpleGrantedAuthority는 GrantedAuthority의 구현 클래스
        // 인증 = 로그인 / 인가 = 접근 제한 -> 권한정보가 필요
        // 인가 -> MemberInfo의 getAuthorities()를 가지고 쳌

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
