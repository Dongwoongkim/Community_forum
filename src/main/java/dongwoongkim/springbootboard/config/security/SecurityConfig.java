package dongwoongkim.springbootboard.config.security;
import dongwoongkim.springbootboard.config.security.details.MemberDetailsService;
import dongwoongkim.springbootboard.token.TokenService;
import dongwoongkim.springbootboard.handler.JwtAccessDeniedHandler;
import dongwoongkim.springbootboard.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenService tokenService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final MemberDetailsService memberDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.   // 토큰 사용하는 방식이므로 csrf disable
                csrf().disable()

                .exceptionHandling()  // 예외 처리 핸들러 등록
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()

                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션 사용하지 않기 위해 stateless로 설정
                .and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/home", "/sign-up", "/login").permitAll() // 접근가능

                .antMatchers(HttpMethod.DELETE, "/api/categories/**").hasAuthority("ADMIN") // 카테고리 삭제, 생성은 ADMIN만 가능
                .antMatchers(HttpMethod.POST, "/api/categories/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/post/**").authenticated() // 게시물 생성은 인증된 사용자만 가능
                .antMatchers(HttpMethod.DELETE, "/api/post/{id}/**").access("@postGuard.check(#id)") // 글은 본인이나 admin만 삭제가능
                .antMatchers(HttpMethod.PATCH, "/api/post/{id}/**").access("@postGuard.check(#id)") // 글은 본인이나 admin만 수정가능

                .antMatchers(HttpMethod.DELETE, "/api/member/{id}/**").access("@memberGuard.check(#id)") // 회원 삭제는 본인이나 admin만 가능
                .antMatchers(HttpMethod.DELETE, "/api/comments/{id}/**").access("@commentGuard.check(#id)") // 댓글은 본인이나 admin만 삭제가능

                .antMatchers(HttpMethod.GET, "/api/messages/send", "/api/messages/receive").authenticated() // 수신 및 송신 쪽지 전체 조회는 토큰 인증
                .antMatchers(HttpMethod.GET, "/api/messages/{id}").access("@messageGuard.check(#id)") // 쪽지 id 조회는 받은사람 or 보낸사람 or ADMIN만 조회 가능
                .antMatchers(HttpMethod.DELETE, "/api/messages/receive/{id}/**").access("@messageReceiveGuard.check(#id)") // 받은 메시지는 받은 사람 or ADMIN만 삭제 가능
                .antMatchers(HttpMethod.DELETE, "/api/messages/send/{id}/**").access("@messageSendGuard.check(#id)") // 보낸 메시지는 보낸 사람 or ADMIN만 삭제 가능

                .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                .antMatchers("/swagger-uri/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()

                .and()
                .userDetailsService(memberDetailsService)
                .apply(new JwtSecurityConfig(tokenService));// JWT 필터 등록

        return http.build();
    }
}
