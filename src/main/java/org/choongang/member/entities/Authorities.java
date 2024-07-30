package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseEntity;
import org.choongang.member.constants.Authority;

@Data
@Entity
@Builder
@IdClass(AuthoritiesId.class) // 복합 키(composite key)를 정의할 때 사용, @IdClass 어노테이션은 별도의 클래스를 만들어 이 클래스가 엔티티의 복합 키 역할을 하도록 지정
@NoArgsConstructor @AllArgsConstructor
public class Authorities extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Id
    @Column(length = 20)
    @Enumerated(EnumType.STRING) // 숫자말고 문자로
    private Authority authority;
}
