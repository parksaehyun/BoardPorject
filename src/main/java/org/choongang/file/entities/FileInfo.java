package org.choongang.file.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseMemberEntity;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class FileInfo extends BaseMemberEntity {
    @Id @GeneratedValue
    private Long seq; // 서버에 업로드 될 파일 이름 - seq, 확장자

    @Column(length = 45, nullable = false)
    private String gid = UUID.randomUUID().toString(); // 그룹 ID // 파일을 그룹으로 관리하기 위한 ID // UUID.randomUUID().toString() : 유니크 아이디?

    @Column(length = 45)
    private String location; // 그룹 안에 세부 위치 // 파일이 실제로 저장된 경로

    @Column(length = 80, nullable = false)
    private String fileName; // 사용자 친화적 파일 이름

    @Column(length = 30) // 확장자가 없는 경우도 있으니 nullable true로
    private String extension; // 파일 확장자

    @Column(length = 80)
    private String contentType; // 파일 형식

    private boolean done; // 글을 쓸 때 이미지 중간에 첨부했다가 창나가기 하는 경우도 있음 -> 찐으로 저장하는 게시글의 이미지만 저장하도록

    @Transient // db에 저장되지 않음 // 2차 가공
    private String fileUrl; // 파일 접근 URL

    @Transient
    private String filePath; // 파일 업로드 경로

}
