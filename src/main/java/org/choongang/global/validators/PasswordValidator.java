package org.choongang.global.validators;

public interface PasswordValidator {
    /**
     * 알파벳 복잡성 체크
     *
     * @param password
     * @param caseInsensitive - false : 대소문자 각각 1개씩 이상 포함, true - 대소문자 구분 X
     * @return
     */
    default boolean alphaCheck(String password, boolean caseInsensitive) {
        if (caseInsensitive) { // 대소문자 구분없이 알파벳 체크
            return password.matches(".*[a-zA-Z]+.*");
        }

        return password.matches(".*[a-z+.*]+.*") && password.matches(".*[A-Z]+.*");
    }
    /**
     * 숫자 복잡성 체크
     *
     * @param password
     * @return
     */
    default boolean numberCheck(String password) {
        boolean matched = password.matches(".*[0-9].*"); // 숫자가 1개 이상 포함되어 있는지 쳌
        return matched;
    }
    /**
     * 특수문자 복잡성 체크
     *
     * @param password
     * @return
     */
    default boolean specialCharsCheck(String password) {
        // 특수문자가 1개 이상 포함되어 있는지
        String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣]+.*";
        // 숫자가 아니어야하고  // 알파벳이 아닌 것 // 한글이 아닌 것 -> 배제하는 방식
        // 남는것은 특수문자
        return password.matches(pattern);
    }
}