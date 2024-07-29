package org.choongang.member.controllers;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestJoin { // 커맨드 객체 : 사용자가 작성한 데이터를 전달하는 역할
    // 기본검증은 빈밸리데이션api가 해줌

    @NotBlank // 필수항목 검증
    @Email // 이메일 검증
    private String email;

    @NotBlank
    @Size(min = 8) // 최소 8자리 이상 작성
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String userName;

    @NotBlank
    private String mobile; // 핸드폰 번호

    @AssertTrue // 반드시 true가 되어야만 통과
    private boolean agree;
}
