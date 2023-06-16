package dongwoongkim.springbootboard.controller.comment;

import dongwoongkim.springbootboard.aop.AssignMemberId;
import dongwoongkim.springbootboard.dto.comment.CommentCreateRequestDto;
import dongwoongkim.springbootboard.dto.comment.CommentResponseDto;
import dongwoongkim.springbootboard.dto.comment.cond.CommentReadCondition;
import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Comment Controller", tags = "Comment")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 목록 조회", notes = "댓글 목록을 조회합니다.")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response readAll(@Valid CommentReadCondition cond) {
        return Response.success(commentService.readAll(cond));
    }

    @ApiOperation(value = "댓글 생성", notes = "댓글을 생성합니다.")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @ModelAttribute CommentCreateRequestDto commentCreateRequestDto) {
        commentService.create(commentCreateRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "댓글을 삭제합니다.", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response delete(@ApiParam(name = "삭제할 댓글 Id", required = true) @PathVariable Long id) {
        commentService.delete(id);
        return Response.success();
    }


}
