package dongwoongkim.springbootboard.advice;

import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.exception.auth.*;
import dongwoongkim.springbootboard.exception.category.CannotConvertNestedStructureException;
import dongwoongkim.springbootboard.exception.category.CategoryNotFoundException;
import dongwoongkim.springbootboard.exception.image.FileUploadFailureException;
import dongwoongkim.springbootboard.exception.image.UnsupportedImageFormatException;
import dongwoongkim.springbootboard.exception.member.DuplicateEmailException;
import dongwoongkim.springbootboard.exception.member.DuplicateUsernameException;
import dongwoongkim.springbootboard.exception.member.InputFormException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.exception.role.RoleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        e.printStackTrace();
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
        return Response.failure(406, "회원가입 양식에 맞기 입력해주세요.");
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

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response loginFailureException(LoginFailureException e) {
        return Response.failure(404, "로그인에 실패햐였습니다.");
    }

    @ExceptionHandler(CannotConvertNestedStructureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertNestedStructureException e) {
        return Response.failure(500,"카테고리를 변경할 수 없습니다.");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException(CategoryNotFoundException e) {
        return Response.failure(404,"카테고리를 찾을 수 없습니다.");
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        return Response.failure(404,"파일 업로드에 실패햐였습니다.");
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException(UnsupportedImageFormatException e) {
        return Response.failure(404,"지원하지 않는 파일형식 입니다.");
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return Response.failure(400, "양식에 맞게 입력해주세요.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(MethodArgumentNotValidException e) {
        return Response.failure(400, "양식에 맞게 입력해주세요.");
    }
}

