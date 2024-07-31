package org.choongang.global.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@MappedSuperclass // 공통속성화 클래스이다 알려주는 애노테이션
@EntityListeners(AuditingEntityListener.class) // 이벤트 리스너 : 변화 감지 -> 변화감지를 위해 계속 돌아가기 때문에 자원소비가됨 -> @EnableJpaAuditing 설정클래스에 넣어주기
public abstract class BaseMemberEntity extends BaseEntity {

    @CreatedBy // 수정불가 // 처음가입일자는 수정 못하게 해야함
    @Column(length = 65, updatable = false)
    private String createdBy;

    @LastModifiedBy // 추가불가, 수정할 때 값 추가 가능
    @Column(length = 65, insertable = false)
    private String modifiedBy;

}
