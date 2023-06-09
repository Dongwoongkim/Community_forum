package dongwoongkim.springbootboard.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "로그인 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {

    @ApiModelProperty(value = "ID", notes = "아이디를 입력해주세요", required = true)
    @Email(message = "아이디 형식을 맞춰주세요.")
    private String username;

    @ApiModelProperty(value = "PW", notes = "비밀번호를 입력해주세요", required = true)
    @NotNull
    private String password;

}
