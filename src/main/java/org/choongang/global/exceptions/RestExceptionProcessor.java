package org.choongang.global.exceptions;

import org.choongang.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

public interface RestExceptionProcessor {
    // ✨rest 컨트롤러 처리

    @ExceptionHandler(Exception.class) // 예외 받기
    default ResponseEntity<JSONData> errorHandler(Exception e) {
        // <JSONData> : 제이슨 형태로 에러 응답하기
        // 반환값 : ResponseEntity : 응답헤더와 바디를 상세하게 설정하기 위해서 에러는 응답코드를 상세하게 설정해주어야 함 -> 응답코드를 상세하게 설정해주기 위해  ResponseEntity 사용
        // 일반 컨트롤러쪽은 모델엔 뷰 스테이터스 통해서 상태코드 설정해줌

        Object message = e.getMessage(); // ✨일반적인 에러 메세지 (기본값), 그냥 던진에러 , 커맨드 객체 에러 아님

        // 내가 정의하지 않은 에러
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본에러는 500으로 고정 // 내가 정의한 에러가 아니면 500

        // 내가 정의한 에러 (= CommonException의 하위 클래스 = 여기엔 상태코드가 있음)
        // 우리가 정의한 예외는 다양한 응답코드가 있음
        // instance of 로 출처 쳌 하고 응답코드 가져와서 적재적소에 맞는 응답코드 내보내기
        if (e instanceof CommonException commonException) {
            status = commonException.getStatus(); // getStatus() : 응답상태코드 가져오기
            // 에러에 따라 달리 출력

            // ✨커맨드 객체 검증에서 발생한 에러
            Map<String, List<String>> errorMessages = commonException.getErrorMessages();
            if (errorMessages != null ) message = errorMessages;
            // errorMessages != null  : 커맨드 객체 검증 에러 // errorMessages : 커맨드 객체 발생시 가공된 에러메세지 문구(밸리데이션 프로퍼티즈)
        }

        // 실패시 // 에러를 제이슨 형태로 변환
        JSONData jsonData = new JSONData();
        jsonData.setSuccess(false);
        jsonData.setMessage(message);
        jsonData.setStatus(status); // status 응답 상태 코드

        e.printStackTrace();

        return ResponseEntity.status(status).body(jsonData); // 응답 상태코드 변경해서 응답
    }
}
