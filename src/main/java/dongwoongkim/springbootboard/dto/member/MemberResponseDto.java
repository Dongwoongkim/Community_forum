package dongwoongkim.springbootboard.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import dongwoongkim.springbootboard.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@ApiModel(value = "회원조회 응답")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    private String email;

    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .nickname(member.getNickname()).build();
    }
}
