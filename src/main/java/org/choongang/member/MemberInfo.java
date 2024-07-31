package org.choongang.member;

import lombok.Builder;
import lombok.Data;
import org.choongang.member.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
public class MemberInfo implements UserDetails {

    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정 -> 인가(접근 통제)를 위해 필요
        return authorities;
    }

    @Override
    public String getPassword() { // 인증에 필요
        return password;
    }

    @Override
    public String getUsername() { // 인증에 필요
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨있냐 안잠겨 있냐
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비번이 만료되었는가
        // 비번만료 시 초기화할 수 있는 페이지로 이동할 수 있게 하면 됨
        return true;
        //return false;
    }

    @Override
    public boolean isEnabled() {
        // 회원탈퇴여부 체크 -> 회원탈퇴 시 false로 바꿈
        return true;
    }
}
