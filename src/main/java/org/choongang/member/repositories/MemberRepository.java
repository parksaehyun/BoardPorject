package org.choongang.member.repositories;

import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
    // QuerydslPredicateExecutor : 검색조건이 많거나 조건이 까다로울 때 사용

    /**
     * [@EntityGraph(attributePaths = "authorities") 쓴 이유]
     * 회원이 없으면 추가 있으면 수정 + 권한설정도 바로 하게 될 거임
     * -> 권한 설정 바로한다
     * -> 바로 가져와야겠다
     * -> 근데 지금은 @OneToMany가LAZY = 지연로딩인 상태
     * -> 일부항목 바로 조회
     * -> @EntityGraph(attributePaths = "authorities") = 권한 처음부터 가져오기
     */

    // MemberSaveService
    @EntityGraph(attributePaths = "authorities") // 즉시로딩
    Optional<Member> findByEmail(String username); // 회원이 없으면 추가 있으면 수정 -> 옵셔널

    // JoinValidator
    default boolean exists(String email) {
        // exists(Predicate predicate)메서드를 가지고 회원이 db에 있냐 없냐 처리할거임
        QMember member = QMember.member;

        return exists(member.email.eq(email));
    }
}