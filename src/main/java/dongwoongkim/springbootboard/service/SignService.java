package dongwoongkim.springbootboard.service;

import dongwoongkim.springbootboard.domain.*;
import dongwoongkim.springbootboard.dto.LoginRequestDto;
import dongwoongkim.springbootboard.dto.MemberResponseDto;
import dongwoongkim.springbootboard.dto.SignUpRequestDto;
import dongwoongkim.springbootboard.dto.LogInResponseDto;
import dongwoongkim.springbootboard.exception.DuplicateUsernameException;
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
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)));
        memberRepository.save(member);
    }

    public LogInResponseDto signIn(LoginRequestDto loginRequestDto) {
        // userpasswordToken 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String jwt = tokenService.createToken(authentication);
        return LogInResponseDto.toDto(jwt);
    }

    private void validateDuplicateSingUpInfo(SignUpRequestDto signUpRequestDto) {
        Member member = memberRepository.findOneWithRolesByUsername(signUpRequestDto.getUsername()).orElse(null);
        if (member != null) {
            throw new DuplicateUsernameException("해당 아이디는 이미 등록된 아이디입니다.");
        }
    }

    public MemberResponseDto getMemberWithAuthoritiesForUser() {
        return MemberResponseDto.toDto(SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithRolesByUsername).orElse(null));
    }
}
