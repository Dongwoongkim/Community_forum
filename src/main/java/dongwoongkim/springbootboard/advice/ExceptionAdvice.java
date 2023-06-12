package dongwoongkim.springbootboard.advice;

import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.exception.auth.AuthenticationEntryPointException;
import dongwoongkim.springbootboard.exception.auth.ValidateTokenException;
import dongwoongkim.springbootboard.exception.member.DuplicateEmailException;
import dongwoongkim.springbootboard.exception.member.DuplicateUsernameException;
import dongwoongkim.springbootboard.exception.auth.IllegalAuthenticationException;
import dongwoongkim.springbootboard.exception.member.InputFormException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.exception.role.RoleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(500, "오류가 발생하였습니다");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException(MemberNotFoundException e) {
        return Response.failure(404, "요청한 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler(IllegalAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response illegalAuthenticationException(IllegalAuthenticationException e) {
        return Response.failure(401, "유효하지 않은 권한입니다.");
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException(RoleNotFoundException e) {
        return Response.failure(404, "요청한 권한 등급을 찾을 수 없습니다.");
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateUsernameException(DuplicateUsernameException e) {
        return Response.failure(409, "중복된 Username입니다.");
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateEmailException(DuplicateEmailException e) {
        return Response.failure(409, "중복된 Email입니다.");
    }

    @ExceptionHandler(InputFormException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Response inputFormException(InputFormException e) {
        return Response.failure(406, "잘못된 회원가입 입력 양식입니다.");
    }

    @ExceptionHandler(ValidateTokenException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Response inputFormException(ValidateTokenException e) {
        return Response.failure(406, "비정삭적인 시도입니다.");
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response accessDeniedException(AccessDeniedException e) {
        return Response.failure(401, "접근 불가능한 권한 입니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response authenticationEntryPointException(AuthenticationEntryPointException e) {
        return Response.failure(403, "인증되지 않은 사용자입니다.");
    }
}
