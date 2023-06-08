package dongwoongkim.springbootboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dongwoongkim.springbootboard.domain.Member;
import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Size(min=2, message = "사용자 이름이 너무 짧습니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    @Size(min = 3, max = 50)
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "닉네임이 너무 짧습니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 한글 또는 알파벳만 입력해주세요.")
    private String nickname;

    @Email(message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    public static SignUpRequestDto toDto(Member member) {
        return SignUpRequestDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .nickname(member.getNickname()).build();
    }
}
