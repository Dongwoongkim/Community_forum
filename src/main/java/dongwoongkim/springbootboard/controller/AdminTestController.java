package dongwoongkim.springbootboard.controller;

import dongwoongkim.springbootboard.dto.MemberResponseDto;
import dongwoongkim.springbootboard.dto.SignUpRequestDto;
import dongwoongkim.springbootboard.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminTestController {
    private final SignService signService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MemberResponseDto> findMyInfo() {
        return ResponseEntity.ok(signService.getMemberWithAuthoritiesForUser());
    }
}
