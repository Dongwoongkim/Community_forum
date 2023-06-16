package dongwoongkim.springbootboard.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import dongwoongkim.springbootboard.dto.post.read.condition.PostDto;
import dongwoongkim.springbootboard.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private MemberDto memberDto;
    private List<CommentResponseDto> children;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static List<CommentResponseDto> toDtoList(List<Comment> comments) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                comments,
                c -> CommentResponseDto.toDto(c),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());

        return helper.convert();
    }

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getId(),
                comment.isDeleted() ? null : comment.getContent(),
                comment.isDeleted() ? null : MemberDto.toDto(comment.getMember()),
                new ArrayList<>(),
                comment.getCreatedTime());
    }
}
