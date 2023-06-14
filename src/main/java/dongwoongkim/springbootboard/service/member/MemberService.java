package dongwoongkim.springbootboard.service.member;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.dto.member.MemberResponseDto;
import dongwoongkim.springbootboard.exception.member.InputFormException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto getMemberWithAuthoritiesForUser() {
        return MemberResponseDto.toDto(SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithRolesByUsername).orElseThrow(MemberNotFoundException::new));
    }

    public MemberResponseDto getMemberWithAuthoritiesForAdmin(Long id) {
        return MemberResponseDto.toDto(memberRepository.findOneWithRolesById(id)
                .orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        log.info("member = {}", member.getUsername());
        memberRepository.delete(member);
    }

}
