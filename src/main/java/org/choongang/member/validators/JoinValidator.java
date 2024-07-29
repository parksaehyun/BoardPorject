package org.choongang.member.validators;

import lombok.RequiredArgsConstructor;
import org.choongang.global.validators.MobileValidator;
import org.choongang.global.validators.PasswordValidator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator, MobileValidator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {// 검증하고자 하는 커맨드 객체를 한정할 때 사용 // 리퀘스트 조인 커맨드 객체만 검증하도록 제한
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) { // Object target : 커맨드 객체
        if (errors.hasErrors()) {
            return; // 이부분 잘 모르겠...
        }

        /**
         * 1. 이미 가입된 회원인지 체크 -> 엔티티 구현해야 함
         * 2. 비밀번호, 비밀번호 확인 일치 여부
         * 3. 비밀번호 복잡성 체크
         * 4. 휴대전화번호 형식 체크
         */

        RequestJoin form = (RequestJoin) target; // Object target 형변환
        String email = form.getEmail(); // 커맨드 객체에서 꺼내오기
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        String mobile = form.getMobile();

        // 1. 이미 가입된 회원인지 체크
        if (memberRepository.exists(email)){
            errors.rejectValue("email", "Duplicated");
        }

        // 2. 비밀번호, 비밀번호 확인 일치 여부
        if (!password.equals(confirmPassword)) {
            // validation.properties에 Mismatch.password와 연동할거임
            errors.rejectValue("confirmPassword", "Mismatch.password"); //rejectValue : 필드 오류
            // Mismatch.password : validation.properties의 키값
        }

        // 3. 비밀번호 복잡성 체크 - 알파벳 대소문자 각각 1개 이상, 숫자 1개 이상, 특수문자 1개 이상
        if (!alphaCheck(password, false) || !numberCheck(password) || !specialCharsCheck(password)) {
            errors.rejectValue("password", "Complexity");
        }

        // 4. 휴대전화번호 형식 체크
        if (!mobileCheck(mobile)) {
            errors.rejectValue("mobile", "Mobile");
        }
    }
}
