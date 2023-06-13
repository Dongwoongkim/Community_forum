package dongwoongkim.springbootboard.config.security;
import dongwoongkim.springbootboard.service.MemberDetailsService;
import dongwoongkim.springbootboard.service.TokenService;
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
                .antMatchers(HttpMethod.DELETE, "/api/member/{id}/**").access("hasAuthority('ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/categories/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/categories/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/posts/**").authenticated()
                .antMatchers(HttpMethod.GET, "/image/**").permitAll()

                .antMatchers("/swagger-uri/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                .and()
                .userDetailsService(memberDetailsService)
                .apply(new JwtSecurityConfig(tokenService));// JWT 필터 등록
        return http.build();
    }
}
