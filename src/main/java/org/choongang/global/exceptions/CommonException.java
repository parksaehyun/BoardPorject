package org.choongang.global.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class CommonException extends RuntimeException {
    // CommonException : 모든예외는 여기로 유입되서 분기됨

    private HttpStatus status;
    private Map<String, List<String>> errorMessages;

    public CommonException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR); // 기본 응답 코드는 500
    }

    public CommonException(String message, HttpStatus status) {
        super(message); // 예외처린는 런타임익셉션이 하도록 넘김
        this.status = status; // 예외상태코드는 우리가 처리
    }
}
