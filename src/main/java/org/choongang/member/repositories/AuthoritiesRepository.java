package org.choongang.member.repositories;

import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.AuthoritiesId;
import org.choongang.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesId>, QuerydslPredicateExecutor<Authorities> {
    // 기본키가 AuthoritiesId 쳌
    // JpaRepository : jpa활용한 쿼리
    // QuerydslPredicateExecutor : 복잡한 쿼리시 사용

    List<Authorities> findByMember(Member member);
}