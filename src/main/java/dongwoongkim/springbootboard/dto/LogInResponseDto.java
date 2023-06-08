package dongwoongkim.springbootboard.dto;

import lombok.*;

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
