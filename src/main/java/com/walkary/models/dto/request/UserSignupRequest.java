package com.walkary.models.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record UserSignupRequest(
        @NotEmpty(message = "아이디가 누락되었습니다.")
        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문과 숫자만 입력 가능합니다.")
        String userId,

        @NotEmpty(message = "비밀번호가 누락되었습니다.")
        @Size(min = 8, max = 16, message = "비밀번호는 8글자에서 16글자 사이여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "비밀번호는 최소 한 개의 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.")
        String password,

        @NotEmpty(message = "아이디가 누락되었습니다.")
        @Size(min = 2, max = 10, message = "이름은 2글자에서 10글자 사이여야 합니다.")
        String username,

        @NotEmpty(message = "메일주소가 누락되었습니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[\\w.-]+$", message = "이메일 주소는 '@' 다음에 '.'이 와야 합니다.")
        String email,

        @NotEmpty(message = "연락처가 누락되었습니다.")
        @Pattern(regexp = "^[0-9]+$", message = "전화번호는 숫자만 포함해야 합니다.")
        String phoneNumber
) {
}
