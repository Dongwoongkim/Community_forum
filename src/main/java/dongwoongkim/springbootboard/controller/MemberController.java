package dongwoongkim.springbootboard.controller;

import dongwoongkim.springbootboard.dto.MemberResponseDto;
import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.service.MemberService;
import dongwoongkim.springbootboard.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Api(value = "Member Controller", tags = "Member")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "자신의 정보 조회", notes = "액세스 토큰과 함께 요청")
    @GetMapping("/member/me")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberResponseDto> findMyInfo() {

        return ResponseEntity.ok(memberService.getMemberWithAuthoritiesForUser());
    }

    @ApiOperation(value = "회원 삭제", notes = "액세스 토큰에 ADMIN 권한 정보 필요")
    @DeleteMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "사용자 id", required = true) @PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "회원 조회", notes = "액세스 토큰에 ADMIN 권한 정보 필요")
    @GetMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "사용자 id", required = true) @PathVariable Long id) {
        return Response.success(memberService.getMemberWithAuthoritiesForAdmin(id));
    }

}
