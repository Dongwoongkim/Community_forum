package dongwoongkim.springbootboard.dto.comment;

import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.exception.post.PostNotFoundException;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@ApiModel(value = "댓글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDto {


    @ApiModelProperty(value = "댓글 내용", notes = "댓글 내용을 입력해주세요", required = true, example = "comment content~")
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "게시글 ID", notes = "게시글 ID을 입력해주세요", required = true, example = "1")
    @NotNull(message = "게시글 ID를 입력해주세요.")
    private Long postId;

    @ApiModelProperty(value = "부모 댓글 아이디", notes = "부모 댓글 아이디를 입력해주세요", example = "7")
    private Long parentCommentId;

}
