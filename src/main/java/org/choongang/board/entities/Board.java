package org.choongang.board.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseMemberEntity;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Board extends BaseMemberEntity {
    // 게시판 설정 엔티티
    // 등록한 관리자가 누구인지 기록

    @Id
    @Column(length=30)
    private String bId; // 게시판 id

    @Column(length=60, nullable = false)
    private String bName; // 게시판 이름
}