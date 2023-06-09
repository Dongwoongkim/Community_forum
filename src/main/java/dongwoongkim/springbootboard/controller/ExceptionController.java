package dongwoongkim.springbootboard.controller;

import dongwoongkim.springbootboard.exception.AccessDeniedException;
import dongwoongkim.springbootboard.exception.AuthenticationEntryPointException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ExceptionController {

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }

    @GetMapping("/exception/entry-point")
    public void authenticateException() {
        throw new AuthenticationEntryPointException();
    }
}
