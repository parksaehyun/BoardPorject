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

        //⭐ 회원조회
        // 유저네임 = 이메일로 설정함
        // 회원정보 조회하고 없을 시에는 스프링시큐리티가 이미 정해놓은 예외(UsernameNotFoundException)를 던져주면 됨
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username)); // orElseThrow : 로그인 상태인데 회원을 지워버린 경우

        // ⭐ 회원의 권한조회 시 없을 경우에 대한 처리?
        List<Authorities> temp = Objects.requireNonNullElse(member.getAuthorities(), List.of(Authorities.builder().member(member).authority(Authority.USER).build()));
        // Objects.requireNonNullElse()의 2번째 매개변수 : null일때의 기본값
        // 없을 때는 일반 사용자로 권한 넣어줌

        // ⭐ 회원 권한 조회(권한에대한 정보를 알려줘야 스프링시큐리티에서 인가를 해줌)
        List<SimpleGrantedAuthority> authorities = temp.stream() // 가공
                .map(a -> new SimpleGrantedAuthority(a.getAuthority().name())).toList();
                // a = authority(MemberInfo의 getAuthorities) // map : 변환작업 // (a.getAuthority().name()) : 매개변수 이넘상수 안되고 문자열이 들어가야 함
        // SimpleGrantedAuthority는 GrantedAuthority의 구현 클래스
        // 스프링시큐리티는 인증과 인가기능을 제공해줌
        // 인증 = 로그인 / 인가 = 접근 제한 -> 권한정보가 필요
        // 인가 -> MemberInfo의 getAuthorities()를 가지고 쳌

        // ⭐ UserDetails의 구현체(MemberInfo) 객체 생성
        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
