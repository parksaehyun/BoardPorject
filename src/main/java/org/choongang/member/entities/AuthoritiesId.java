package org.choongang.member.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.choongang.member.constants.Authority;

@EqualsAndHashCode // 중복제거의 기준 // 기본키는 유일해야함(중복x)
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesId {
    private Member member;
    private Authority authority;
}
