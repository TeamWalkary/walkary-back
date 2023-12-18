package com.walkary.models.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 로그인 요청 DTO
 * id, pw 필드중 어느게 틀린지 모르도록 해야 보안에 유리함
 */
public record UserLoginRequest(
        @NotEmpty(message = "로그인 정보가 유효하지 않습니다.")
        @Size(max = 20, message = "로그인 정보가 유효하지 않습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "로그인 정보가 유효하지 않습니다.")
        String userId,

        @NotEmpty(message = "로그인 정보가 유효하지 않습니다.")
        @Size(max = 16, message = "로그인 정보가 유효하지 않습니다.")
        String password
) {
}
