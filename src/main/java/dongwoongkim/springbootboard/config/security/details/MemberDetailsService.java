package dongwoongkim.springbootboard.config.security.details;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findOneWithRolesByUsername(username).
                orElseThrow(MemberNotFoundException::new));
        return createUser(member.get());
    }

    private UserDetails createUser(Member member) {
        List<SimpleGrantedAuthority> grantedAuthorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleType().name()))
                .collect(Collectors.toList());

        return new User(member.getUsername(), member.getPassword(), grantedAuthorities);
    }
}
