package dongwoongkim.springbootboard.service;

import dongwoongkim.springbootboard.dto.MemberResponseDto;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto getMemberWithAuthoritiesForUser() {
        return MemberResponseDto.toDto(SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithRolesByUsername).orElse(null));
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
