package org.choongang.global.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass // 공통속성화 클래스이다 알려주는 애노테이션
@EntityListeners(AuditingEntityListener.class) // 이벤트 리스너 : 변화 감지 -> 변화감지를 위해 계속 돌아가기 때문에 자원소비가됨 -> @EnableJpaAuditing 설정클래스에 넣어주기
public abstract class BaseEntity {
    //abstract : 추상클래스 애를 상속받으면 멤버변수 데이터 다들 공유 가능 = 공통 속성화
    // 근데 알려주어야함 = @MappedSuperclass = 이걸 정의해야 공통속성이라 인식
    // @MappedSuperclass : 공통속성으로 사용할 상위클래스에 정의해야하는 애노테이션

    @CreatedDate
    @Column(updatable = false) // 수정불가 // 처음가입일자는 수정 못하게 해야함
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false) // 추가불가, 수정할 때 값 추가 가능
    private LocalDateTime modifiedAt;

    @Column(insertable = false) // 추가불가, 수정할 때 값 추가 가능
    private LocalDateTime deletedAt; // 일정시간이 지나면 자동 삭제
}
