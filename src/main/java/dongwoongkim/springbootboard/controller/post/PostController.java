package dongwoongkim.springbootboard.controller.post;

import dongwoongkim.springbootboard.aop.AssignMemberId;
import dongwoongkim.springbootboard.dto.post.PostCreateRequestDto;
import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
