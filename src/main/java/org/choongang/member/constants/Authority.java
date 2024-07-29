package org.choongang.member.constants;

public enum Authority {
    // 권한은 추가 될 수 있으니 lIST형태로 담아줄것
    // 한사람이 여러 권한을 가질수 있음
    // -> 어떤 관리자는 상담과 주문권한을 가지고 있음
    // -> 이런경우 테이블을 분리해 주어야 함
    USER,
    ADMIN
}
