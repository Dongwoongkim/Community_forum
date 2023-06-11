package dongwoongkim.springbootboard.dto.member;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel(value = "로그인 응답")
@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LogInResponseDto {

    private String token;
    public static LogInResponseDto toDto(String token) {
        return new LogInResponseDto(token);
    }
}
