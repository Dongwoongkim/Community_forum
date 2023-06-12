package dongwoongkim.springbootboard.service;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = Optional.ofNullable(memberRepository.findOneWithRolesByUsername(username).
                orElseThrow(() -> new MemberNotFoundException("DB에서 아이디와 일치하는 회원을 찾을 수 없습니다.")));
        log.info("loadUserByUsername executed.");
        return createUser(username, member.get());
    }

    private User createUser(String username, Member member) {
        List<SimpleGrantedAuthority> grantedAuthorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleType().name()))
                .collect(Collectors.toList());

        return new User(username,member.getPassword(),grantedAuthorities);
    }
}
