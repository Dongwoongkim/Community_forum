package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard{
    private final MemberRepository memberRepository;
    @Override
    protected boolean isResourceOwner(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = AuthHelper.extractMemberId();
        return id.equals(memberId);
    }
}
