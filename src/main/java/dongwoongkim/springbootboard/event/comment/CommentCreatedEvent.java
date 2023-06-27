package dongwoongkim.springbootboard.event.comment;

import dongwoongkim.springbootboard.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
@AllArgsConstructor
public class CommentCreatedEvent {

    // 댓글 작성자
    private MemberDto publisher;


    // 댓글의 상위 댓글 작성자
    private MemberDto parentPublisher;


    // 글쓴이
    private MemberDto postWriter;

    private String content;
}
