package dongwoongkim.springbootboard.controller.exception;

import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.exception.auth.AuthenticationEntryPointException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@Slf4j
public class ExceptionController {

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }

    @GetMapping("/exception/entry-point")
    public void authenticateException() {
        log.info("hi!");
        throw new AuthenticationEntryPointException();
    }
}
