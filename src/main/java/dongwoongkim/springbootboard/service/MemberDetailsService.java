package dongwoongkim.springbootboard.service;

import dongwoongkim.springbootboard.domain.Member;
import dongwoongkim.springbootboard.exception.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = Optional.ofNullable(memberRepository.findOneWithRolesByUsername(username).
                orElseThrow(() -> new MemberNotFoundException()));

        return createUser(username, member.get());
    }

    private User createUser(String username, Member member) {
        List<SimpleGrantedAuthority> grantedAuthorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleType().name()))
                .collect(Collectors.toList());

        return new User(username,member.getPassword(),grantedAuthorities);
    }
}
