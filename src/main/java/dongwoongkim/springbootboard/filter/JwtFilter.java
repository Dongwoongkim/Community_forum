package dongwoongkim.springbootboard.filter;

import dongwoongkim.springbootboard.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZAITON_HEADER = "Authorization";
    private final TokenService tokenService;

    // 토큰의 인증정보 SecurityContext에 저장
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 헤더에서 Authorization 따오기
        String Bearer_token = httpServletRequest.getHeader(AUTHORIZAITON_HEADER);
        String jwt = resolveToken(Bearer_token);

        if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
            Authentication authentication = tokenService.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("인증 정보를 Security Context에 저장했습니다.");
        } else {
            log.debug("JWT가 유효하지 않습니다.");
        }

        chain.doFilter(request,response);
    }

    private String resolveToken(String bearer_token) {
        if (StringUtils.hasText(bearer_token) && bearer_token.startsWith("Bearer ")) {
            log.info("검증중");
            return bearer_token.substring(7);
        }
        log.info("검증실패");
        return null;
    }
}
