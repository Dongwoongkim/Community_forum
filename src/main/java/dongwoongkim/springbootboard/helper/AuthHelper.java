package dongwoongkim.springbootboard.helper;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.member.InputFormException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHelper {
    private final MemberRepository memberRepository;

    public Long extractMemberId() {
        Authentication authentication = getAuthenticationFromContext();
        UserDetails principal = (User) authentication.getPrincipal();
        Member member = memberRepository.findByUsername(principal.getUsername()).orElseThrow(MemberNotFoundException::new);
        return member.getId();
    }
    public boolean isAuthenticated() {
        Authentication authentication = getAuthenticationFromContext();
        return authentication.isAuthenticated() && authentication instanceof UsernamePasswordAuthenticationToken;
    }

    public List<RoleType> extractMemberRolesFromContext() {
        Authentication authentication = getAuthenticationFromContext();
        User principal = (User) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = principal.getAuthorities();

        return authorities.stream().map(authority -> authority.getAuthority()).map(authStr -> RoleType.valueOf(authStr)).collect(Collectors.toList());
    }

    private Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
