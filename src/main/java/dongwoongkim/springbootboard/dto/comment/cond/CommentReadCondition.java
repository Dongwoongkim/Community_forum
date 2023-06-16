package dongwoongkim.springbootboard.dto.comment.cond;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadCondition {

    @NotNull(message = "게시글 번호를 입력해주세요")
    @PositiveOrZero(message = "0 이상의 게시글 번호를 입력하세요")
    private Long postId;

}
