package org.choongang.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component // rest에러 메세지 내가 설정한거 보내주기
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils
    // 필드명, 메세지명 // map형태 - 키 :필드명  값 : 메세지명

    private final MessageSource messageSource; // 설정 디렉토리에 있는 메세지 소스 빈 가져오기
    private final HttpServletRequest request; // 요청쪽의 언어를 알기 위해 가져옴 // 브라우저의 로케일 설정 가져오기

    public String toUpper(String str) {
        return str.toUpperCase();
    }

    public Map<String, List<String>> getErrorMessages(Errors errors) {
        // FieldErrors처리 // 커맨드객체와 관련된 에러 // 필드랑 에러가 매핑된 에러
        Map<String, List<String>> messages = errors.getFieldErrors() // 필드별로 나온 에러 가져옴
                .stream() // 스트림 사용
                .collect(Collectors.toMap (FieldError::getField, e -> getCodeMessages(e.getCodes()))); // 메세지소스 에러코드 가져온거(NotBlank.password 이런코드)
        // toMap : 콜렉트로 모아서 map으로 바꾸기
        // f.getField() : 키
        // e -> getCodeMessages(e.getCodes()))) :메세지명 =  에러코드 -> 문자열 데이터로 바꿔줄거임 -> 그럴러면 가공을 해야함

        // GlobalErrors처리 // 특정 필드를 한정하지 않은 에러 //로그인밸리데이터 참고
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages); // 형식을 맞춰주기 위해서?? 글로벌 에러를 글로벌이라고 키 값 해준건가
        }

        return messages;
    }

    // 에러코드 문자열 데이터로 바꿔줄거임 -> 가공을 해야함
    // 여기서 가공 // 메세지소스코드를 사용자 친화적 멘트로 변환 = 가공
    public List<String> getCodeMessages(String[] codes) {
        // List : 특정 필드의 에러메세지가 여러개 인 상황이 있어서
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource; // 설정 디렉토리에 있는 메세지 소스 빈 가져오기 // 자동으로 템플릿에 연동 되게 설정되어 있지만 json은 템플릿 형태로 반환하는게 아니니 코드적으로 메세지소스 가져오기
        ms.setUseCodeAsDefaultMessage(false); // 메세지가 없으면 코드 그자체로 메세지 출력 -> 예외발생 방지하려고 이렇게 한거임 // 싱글톤임
        // 등록되지 않은 메세지는 필요 없으니 폴스함

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale()); // 요청쪽의 언어를 알기 위해 가져옴 // 브라우저의 로케일 설정 가져오기
                        // 매개변수 : 코드 , 바뀔수 있는 부분, 브라우저의 로케일
                        // 바뀔수 있는 부분 :  LOGIN_MSG={0}({1})님 로그인... // 오브젝트 배열?
                    } catch (Exception e) {
                        return ""; // 등록되지 않은 메세지는 필요 없으니 폴스함 -> 근데 이러면 널포인트익셉션이 발생하니 없을때는 빈값 넣어줌
                    }
                })
                .filter(s -> !s.isBlank())
                .toList(); // 리스트 형태로 반환

        ms.setUseCodeAsDefaultMessage(true); // 싱글톤이라 쓰고나서 다시 트루로 바꿈 // 안그럼 계속 폴스 상태
        // 템플릿쪽은 코드가 없으면 널포인트익셉션 오류 발생함 // 다시 원래 설정으로 바꿔주기

        return messages;
    }

    // 메세지 가져올 수 있는 함수 하나 더 추가
    public String getMessage(String code) {
        List<String> messages = getCodeMessages(new String[]{code});

        return messages.isEmpty() ? code : messages.get(0);
    }
}
