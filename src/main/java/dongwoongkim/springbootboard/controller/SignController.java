package dongwoongkim.springbootboard.controller;

import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.dto.LoginRequestDto;
import dongwoongkim.springbootboard.dto.SignUpRequestDto;
import dongwoongkim.springbootboard.dto.LogInResponseDto;
import dongwoongkim.springbootboard.exception.InputFormException;
import dongwoongkim.springbootboard.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDtoDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InputFormException("회원가입 폼 형식에 맞지 않습니다");
        }

        signService.signUp(signUpRequestDtoDto);
        return Response.success();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LogInResponseDto> signin(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LogInResponseDto logInResponseDto = signService.signIn(loginRequestDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + logInResponseDto.getToken())
                .body(logInResponseDto);
    }
}
