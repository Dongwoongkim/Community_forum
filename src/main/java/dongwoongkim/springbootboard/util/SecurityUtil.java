package dongwoongkim.springbootboard.util;

import dongwoongkim.springbootboard.exception.IllegalAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("No authentication found!");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return Optional.ofNullable(userDetails.getUsername());
        }
        if (principal instanceof String) {
            return Optional.of(principal.toString());
        }

        throw new IllegalAuthenticationException("Authentication이 유효하지 않음.");

    }
}
