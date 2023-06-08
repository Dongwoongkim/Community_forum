package dongwoongkim.springbootboard.service;

import dongwoongkim.springbootboard.domain.*;
import dongwoongkim.springbootboard.dto.LoginRequestDto;
import dongwoongkim.springbootboard.dto.MemberResponseDto;
import dongwoongkim.springbootboard.dto.SignUpRequestDto;
import dongwoongkim.springbootboard.dto.LogInResponseDto;
import dongwoongkim.springbootboard.exception.DuplicateEmailException;
import dongwoongkim.springbootboard.exception.DuplicateUsernameException;
import dongwoongkim.springbootboard.exception.LoginFailureException;
import dongwoongkim.springbootboard.exception.RoleNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.RoleRepository;
import dongwoongkim.springbootboard.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateDuplicateSingUpInfo(signUpRequestDto);
        Member member = new Member(signUpRequestDto.getUsername(),
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                signUpRequestDto.getNickname(),
                signUpRequestDto.getEmail(),
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(() -> new RoleNotFoundException("해당 권한을 찾을 수 없습니다"))));
        memberRepository.save(member);
    }

    public LogInResponseDto signIn(LoginRequestDto loginRequestDto) {
        // userpasswordToken 생성
        String jwt = jwtLoginRequest(loginRequestDto);
        return LogInResponseDto.toDto(jwt);
    }

    private String jwtLoginRequest(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenService.createToken(authentication);

        if (!StringUtils.hasText(jwt)) {
            throw new LoginFailureException("로그인에 실패하였습니다.");
        }
        return jwt;
    }

    private void validateDuplicateSingUpInfo(SignUpRequestDto signUpRequestDto) {
        Member member = memberRepository.findOneWithRolesByUsername(signUpRequestDto.getUsername()).orElse(null);
        if (member != null) {
            throw new DuplicateUsernameException("해당 아이디는 이미 등록된 아이디입니다.");
        } else if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new DuplicateEmailException("해당 이메일은 이미 등록된 이메일입니다.");
        }
    }

}
