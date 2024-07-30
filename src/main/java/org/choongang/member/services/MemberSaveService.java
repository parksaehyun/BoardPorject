package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.constants.Authority;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // 회원추가됬는데 권한은 추가안되면 안되니까 둘 중 하나라도 실패시 둘다 처리 안되도록 롤백, 제약조건때문에 그럴일 없을 것 같지만 그래도 안전하게ㅎㅎ
public class MemberSaveService { // 회원가입 + 회원정보 수정해주는 클래스
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder; // 비크립트

    // 회원이 없으면 추가 있으면 수정 -> 옵셔널 -> MemberRepository에 옵셔널 정의

    /**
     * 회원 가입 처리
     * @param form
     */

    // 매개변수 RequestJoin form : 회원가입만 가능 = 추가만 가능, 수정x 한 save()
    public void save(RequestJoin form) {
        // form쪽에 있는 값을 member에 옮겨 줄거임
        // form이랑 member는 데이터가 비슷 = getter와 setter가 비슷하면 알아서 데이터를 치환 해 줌 -> ModelMapper()
        // ModelMapper는 Java 애플리케이션에서 객체 간의 매핑을 단순화하고 자동화하기 위해 사용되는 라이브러리입니다. 객체 매핑은 DTO(Data Transfer Object)와 엔티티 간의 데이터 전환을 쉽게 해줍니다. 특히, 서로 다른 객체 간의 속성 값을 복사하거나 변환하는 작업을 편리하게 처리할 수 있습니다.

        Member member = new ModelMapper().map(form, Member.class);
        // ModelMapper() : 기본적으로 비슷하게 매칭이 되어있는 getter와 setter패턴을 보고 데이터를 치환해줌
        // 원 소스 : form -> 내가 바꾸고자하는 클래스 : 엔티티의 member클래스

        String hash = passwordEncoder.encode(form.getPassword()); // 비번 해시화(비크립트)
        member.setPassword(hash);

        save(member, List.of(Authority.USER));// 기본권한만 추가
    }

    // 추가, 수정 다 하는 save() -> 주가되는 메서드
    public void save(Member member, List<Authority> authorities) {
        // 권한여부도 업데이트나 추가할 가능성 있어서 List<Authorities> authorities 매개변수에 추가

        // 휴대전화번호 숫자만 기록
        String mobile = member.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        memberRepository.saveAndFlush(member);
        // 권한 추가, 수정 S
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(a -> Authorities.builder()
                    .member(member)
                    .authority(a)
                    .build()).toList();

            authoritiesRepository.saveAllAndFlush(items);
        }
        // 권한 추가, 수정 E
    }
}
