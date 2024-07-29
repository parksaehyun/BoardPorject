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
@IdClass(AuthoritiesId.class)
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
