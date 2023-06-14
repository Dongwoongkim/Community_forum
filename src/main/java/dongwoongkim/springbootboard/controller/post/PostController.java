package dongwoongkim.springbootboard.controller.post;

import dongwoongkim.springbootboard.aop.AssignMemberId;
import dongwoongkim.springbootboard.dto.post.PostCreateRequestDto;
import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Slf4j
public class PostController {
    private final PostService postService;

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성한다.")
    @PostMapping
    @AssignMemberId // AOP로 인증된 회원만 가능
    public Response create(@Valid @ModelAttribute PostCreateRequestDto postCreateRequestDto) {
        log.info("memberId = {}", postCreateRequestDto.getMemberId());
        return Response.success(postService.create(postCreateRequestDto));
    }

    @ApiOperation(value = "게시글 조회", notes = "게시글 번호로 게시글을 조회한다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "조회할 게시글 id", required = true) @PathVariable Long id) {
        return Response.success(postService.read(id));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글 번호로 게시글을 삭제한다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "삭제할 게시글 id", required = true) @PathVariable Long id) {
        postService.delete(id);
        return Response.success();
    }
}
