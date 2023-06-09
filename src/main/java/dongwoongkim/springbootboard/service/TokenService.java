package dongwoongkim.springbootboard.service;
import dongwoongkim.springbootboard.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenService  {
    private final JwtHandler jwtHandler;

    // 토큰 생성
    public String createAccessToken(Authentication authentication) {
        return jwtHandler.createAccessToken(authentication);
    }

    public Authentication getAuthentication(String token) {
        return jwtHandler.getAuthentication(token);
    }

    public boolean validateToken(String token) {
        return jwtHandler.validateToken(token);
    }

}



