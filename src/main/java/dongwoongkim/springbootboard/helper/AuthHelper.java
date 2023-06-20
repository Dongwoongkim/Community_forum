package dongwoongkim.springbootboard.helper;

import dongwoongkim.springbootboard.config.security.guard.MemberDetails;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.member.InputFormException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.token.TokenService;
import lombok.NoArgsConstructor;
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

public class AuthHelper {

    public static Long extractMemberId() {
        Authentication authentication = getAuthenticationFromContext();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return Long.valueOf(principal.getId());
    }
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthenticationFromContext();
        return authentication.isAuthenticated() && authentication instanceof UsernamePasswordAuthenticationToken;
    }

    public static List<RoleType> extractMemberRolesFromContext() {
        Authentication authentication = getAuthenticationFromContext();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        return authorities.stream().map(authority -> authority.getAuthority()).map(authStr -> RoleType.valueOf(authStr)).collect(Collectors.toList());
    }

    private static Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
