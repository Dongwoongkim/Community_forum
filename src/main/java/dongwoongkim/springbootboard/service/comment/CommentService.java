package dongwoongkim.springbootboard.service.comment;

import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.dto.alarm.AlarmInfoDto;
import dongwoongkim.springbootboard.dto.comment.CommentCreateRequestDto;
import dongwoongkim.springbootboard.dto.comment.CommentResponseDto;
import dongwoongkim.springbootboard.dto.comment.cond.CommentReadCondition;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import dongwoongkim.springbootboard.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    // 단일 책임원칙에 의거하여 알람 서비스 분리
    private final ApplicationEventPublisher publisher;

    // readAll
    public List<CommentResponseDto> readAll(CommentReadCondition cond) {
        List<Comment> comments = commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(cond.getPostId());
        return CommentResponseDto.toDtoList(comments);
    }

    // create
    @Transactional
    public void create(@Valid CommentCreateRequestDto commentRequestCreateDto) {
        Comment comment = commentRepository.save(Comment.toEntity(commentRequestCreateDto, postRepository, memberRepository, commentRepository));
        comment.createAlarm(publisher);
    }

    // delete
    @Transactional
    public void delete(@Valid Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        Optional<Comment> deletableComment = comment.findDeletableComment();
        deletableComment.ifPresentOrElse(commentRepository::delete, comment::delete);
    }



}
