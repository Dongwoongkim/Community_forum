package dongwoongkim.springbootboard.controller;

import dongwoongkim.springbootboard.dto.MemberResponseDto;
import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.service.MemberService;
import dongwoongkim.springbootboard.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberResponseDto> findMyInfo() {
        return ResponseEntity.ok(memberService.getMemberWithAuthoritiesForUser());
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        memberService.delete(id);
        return Response.success();

    }
}
