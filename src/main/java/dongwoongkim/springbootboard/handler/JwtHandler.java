package dongwoongkim.springbootboard.handler;

import dongwoongkim.springbootboard.exception.auth.ValidateTokenException;
import dongwoongkim.springbootboard.config.security.details.MemberDetails;
import dongwoongkim.springbootboard.token.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtHandler implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHENTICATE_ID = "ID";
    private final long tokenValidityInMilliseconds;
    private final String originSecretkey;
    private Key secret;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(originSecretkey);
        this.secret = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtHandler(@Value("${jwt.secret}") String originSecretkey, @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds) {
        this.originSecretkey = originSecretkey;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
    }

    public String createAccessToken(Authentication authentication, TokenService.PrivateClaims privateClaims) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .addClaims(Map.of(AUTHORITIES_KEY, authorities))
                .addClaims(Map.of(AUTHENTICATE_ID, privateClaims.getMemberId()))
                .signWith(secret, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthenticationFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        MemberDetails principal = new MemberDetails((String) claims.get(AUTHENTICATE_ID), authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰 검증 true or false
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        throw new ValidateTokenException();
    }
}
