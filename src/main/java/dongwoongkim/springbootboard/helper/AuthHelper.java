package dongwoongkim.springbootboard.helper;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final MemberRepository memberRepository;
    public Long extractMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (User) authentication.getPrincipal();
        Member member = memberRepository.findByUsername(principal.getUsername()).orElseThrow(MemberNotFoundException::new);
        return member.getId();
    }
}
