package org.choongang.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter
public class AlertRedirectException extends AlertException {

    private String url;
    private String target;

    private AlertRedirectException(String message, String url, HttpStatus status, String target) {
        super(message, status);

        target = StringUtils.hasText(target) ? target : "self"; // self = 현재 창

        this.url = url;
        this.target = target;
    }

    public AlertRedirectException(String message, String url, HttpStatus status) {
        // target이 없을 때는 self로 고정
        this(message, url, status, "self");
    }
}
