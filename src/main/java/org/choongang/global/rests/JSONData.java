package org.choongang.global.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor // 데이터 기본생성자도 필요
@RequiredArgsConstructor
public class JSONData {
    // 제이슨 형태로 응답할 때에는 형식을 통일해주기 위한 클래스
    // 매 요청시 마다 형식이 다르면 안되기 때문에 형식 고정
    // 통일된 형식으로 응답하기 위해서 틀을 정한거다
    // 실패 -> 메세지
    // 성공 -> 데이터전송(형식이 다양하게 옴 -> 자료형 Object) + 메세지

    private HttpStatus status = HttpStatus.OK; // 응답코드 // 응답코드도 200이 많으니 고정
    private boolean success = true;  // 보통은 성공값이 많을 거 그래서 true로 고정
    private Object message; // 응답 메시지 // 자료형 Object : 문자도 가능, map도 가능 : 커맨드 객체 검증(map형태)도 하기에
    @NonNull // 데이터없이 응답헤더만 보내는경우도 있음
    private Object data; // 응답 데이터
    // 성공 시에 데이터도 많이 보냄
    // @RequiredArgsConstructor : 데이터 생성자 매개변수로 바로 내보낼 수 있게 할거
    //@NoArgsConstructor // 데이터 기본생성자도 필요 // 데이터없이 응답헤더만 보내는경우도 있음
    // 자료형 Object
}
