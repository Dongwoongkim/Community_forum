package dongwoongkim.springbootboard.service.comment;

import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.dto.comment.CommentCreateRequestDto;
import dongwoongkim.springbootboard.dto.comment.CommentResponseDto;
import dongwoongkim.springbootboard.dto.comment.cond.CommentReadCondition;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
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

    // readAll
    public List<CommentResponseDto> readAll(CommentReadCondition cond) {
        List<Comment> comments = commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(cond.getPostId());
        return CommentResponseDto.toDtoList(comments);
    }

    // create
    @Transactional
    public void create(@Valid CommentCreateRequestDto commentRequestCreateDto) {
        commentRepository.save(Comment.toEntity(commentRequestCreateDto, postRepository, memberRepository, commentRepository));
    }

    // delete
    @Transactional
    public void delete(@Valid Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        Optional<Comment> deletableComment = comment.findDeletableComment();
        deletableComment.ifPresentOrElse(commentRepository::delete, comment::delete);
    }



}
