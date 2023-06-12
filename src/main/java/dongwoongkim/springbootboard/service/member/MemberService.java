package dongwoongkim.springbootboard.service.member;

import dongwoongkim.springbootboard.dto.member.MemberResponseDto;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
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
                .flatMap(memberRepository::findOneWithRolesByUsername).orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다.")));
    }

    public MemberResponseDto getMemberWithAuthoritiesForAdmin(Long id) {
        return MemberResponseDto.toDto(memberRepository.findOneWithRolesById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다.")));
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.delete(
                memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다.")));
    }
}
