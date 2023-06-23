package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
