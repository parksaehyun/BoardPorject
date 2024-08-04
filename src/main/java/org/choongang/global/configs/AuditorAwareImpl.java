package org.choongang.global.configs;

import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {
    // implements AuditorAware : 로그인 사용자가 자동 DB 추가

    private final MemberUtil memberUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
        String email = memberUtil.isLogin() ? memberUtil.getMember().getEmail() : null;

        return Optional.ofNullable(email); // null값 허용하면서 값 담아주기
    }
}
