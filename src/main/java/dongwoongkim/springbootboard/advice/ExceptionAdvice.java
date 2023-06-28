package dongwoongkim.springbootboard.advice;

import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.exception.auth.*;
import dongwoongkim.springbootboard.exception.category.CannotConvertNestedStructureException;
import dongwoongkim.springbootboard.exception.category.CategoryNotFoundException;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.exception.image.CannotFindExtException;
import dongwoongkim.springbootboard.exception.image.FileUploadFailureException;
import dongwoongkim.springbootboard.exception.image.UnsupportedImageFormatException;
import dongwoongkim.springbootboard.exception.member.DuplicateEmailException;
import dongwoongkim.springbootboard.exception.member.DuplicateUsernameException;
import dongwoongkim.springbootboard.exception.member.InputFormException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.exception.message.MessageNotFoundException;
import dongwoongkim.springbootboard.exception.post.PostNotFoundException;
import dongwoongkim.springbootboard.exception.role.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

    private final MessageSource ms;

    // uncatched Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return getFailureResponse("exception.code", "exception.msg");
    }

    // Field Error
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return getFailureResponse("exception.code.bad_request", "exception.msg.binding");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getFailureResponse("exception.code.bad_request", "exception.msg.binding");
    }

    // auth
    @ExceptionHandler(IllegalAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response illegalAuthenticationException(IllegalAuthenticationException e) {
        return getFailureResponse("exception.code.authorize", "exception.msg.authorize");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response accessDeniedException(AccessDeniedException e) {
        return getFailureResponse("exception.code.authorize", "exception.msg.authorize");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response authenticationEntryPointException(AuthenticationEntryPointException e) {
        return getFailureResponse("exception.code.forbidden", "exception.msg.un_login");
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response loginFailureException(LoginFailureException e) {
        return getFailureResponse("exception.code.bad_request", "exception.msg.login");
    }

    @ExceptionHandler(ValidateTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response validateTokenException(ValidateTokenException e) {
        return getFailureResponse("exception.msg.validate_token", "exception.msg.binding");

    }

    // category
    @ExceptionHandler(CannotConvertNestedStructureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertNestedStructureException e) {
        return getFailureResponse("exception.code", "exception.msg.category.convert");

    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException(CategoryNotFoundException e) {
        return getFailureResponse("exception.code.not_found", "exception.msg.category.notfound");
    }

    // comment
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException(CommentNotFoundException e) {
        return getFailureResponse("exception.code.not_found", "exception.msg.comment.notfound");
    }

    // image
    @ExceptionHandler(CannotFindExtException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cannotFindExtException(CannotFindExtException e) {
        return getFailureResponse("exception.code.bad_request", "exception.msg.file.ext.notfound" );

    }
    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        return getFailureResponse("exception.code.bad_request", "exception.msg.file.uploadFail");

    }
    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response unsupportedImageFormatException(UnsupportedImageFormatException e) {
        return getFailureResponse("exception.code.bad_request", "exception.msg.file.un_support");
    }

    // member
    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateEmailException(DuplicateEmailException e) {
        return getFailureResponse("exception.code.conflict", "exception.msg.member.duplicate.email");

    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateUsernameException(DuplicateUsernameException e) {
        return getFailureResponse("exception.code.conflict", "exception.msg.member.duplicate.username");

    }
    @ExceptionHandler(InputFormException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Response inputFormException(InputFormException e) {
        return getFailureResponse("exception.code.not_acceptable", "exception.msg.binding");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException(MemberNotFoundException e) {
        return getFailureResponse("exception.code.not_found", "exception.msg.member.notfound");

    }

    // message
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response messageNotFoundException(MessageNotFoundException e) {
        return getFailureResponse("exception.code.not_found", "exception.msg.message.notfound");
    }

    // post
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response postNotFoundException(PostNotFoundException e) {
        log.info("lang = {}", LocaleContextHolder.getLocale());
        log.info("ms = {}", ms.getClass());
        return getFailureResponse("exception.code.not_found", "exception.msg.post.notfound");
    }

    // role
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException(RoleNotFoundException e) {
        return getFailureResponse("exception.code.not_found", "exception.msg.role.notfound");
    }


    private Response getFailureResponse(String code, String msg) {
        return Response.failure(getCode(code),getMessage(msg));
    }

    private Integer getCode(String code) {
      return Integer.valueOf(ms.getMessage(code,null,null));
    }

    private String getMessage(String msg) {
        return ms.getMessage(msg, null, LocaleContextHolder.getLocale());
    }
}

