package dongwoongkim.springbootboard.domain.comment;

import dongwoongkim.springbootboard.domain.base.BaseEntity;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.comment.CommentCreateRequestDto;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import dongwoongkim.springbootboard.event.comment.CommentCreatedEvent;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.exception.post.PostNotFoundException;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean deleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 부모댓글 삭제 -> 자식댓글 모두 삭제
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    public Comment(String content, Member member, Post post, Comment parent) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.parent = parent;
    }

    public void delete() {
        this.deleted = true;
    }

    public Optional<Comment> findDeletableComment() {
        // 현재 댓글의 자식 댓글이 있으면 실제 삭제 X
        // 현재 댓글의 자식 댓글이 없으면 부모 살펴보기
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    private Comment findDeletableCommentByParent() {
        // 현재 댓글의 부모 댓글 실제 삭제 가능 조건
        // (현재 댓글의 부모 댓글이 존재하고, 부모 댓글이 delete = true이고, 부모 댓글의 자식이 자기 자신 뿐)이면 부모 댓글의 상위 댓글중에도 실제 삭제 가능한 댓글 검색
        // 그렇지 않으면 삭제 가능한 댓글은 현재 댓글 뿐.
//        return isDeletableParent() ? getParent().findDeletableCommentByParent() : this;
        if (isDeletableParent()) {
            Comment deletableParent = getParent().findDeletableCommentByParent();
            if(getParent().getChildren().size()==1) return deletableParent;
        }
        return this;
    }

    private boolean isDeletableParent() {
        return getParent() != null && getParent().isDeleted();
    }

    private boolean hasChildren() {
        return getChildren().size() != 0;
    }

    public static Comment toEntity(CommentCreateRequestDto commentRequestCreateDto, PostRepository postRepository, MemberRepository memberRepository, CommentRepository commentRepository) {
        return new Comment(commentRequestCreateDto.getContent(),
                memberRepository.findById(commentRequestCreateDto.getMemberId()).orElseThrow(MemberNotFoundException::new),
                postRepository.findById(commentRequestCreateDto.getPostId()).orElseThrow(PostNotFoundException::new),
                Optional.ofNullable(
                        commentRepository.findById(commentRequestCreateDto.getParentCommentId()).orElseThrow(CommentNotFoundException::new)).orElse(null)

        );
    }

    public void createAlarm(ApplicationEventPublisher publisher) {
        publisher.publishEvent(
                new CommentCreatedEvent(MemberDto.toDto(getMember()),
                        Optional.ofNullable(getParent()).map(p -> p.getMember()).map(m -> MemberDto.toDto(m)).orElseGet(()-> MemberDto.empty()),
                        MemberDto.toDto(getPost().getMember()),
                        getContent()
        ));

    }
}
