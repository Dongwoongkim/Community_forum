package dongwoongkim.springbootboard.repository.comment;

import dongwoongkim.springbootboard.config.querydsl.QuerydslConfig;
import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.repository.category.CategoryRepository;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static dongwoongkim.springbootboard.factory.category.CategoryFactory.createCategory;
import static dongwoongkim.springbootboard.factory.comment.CommentFactory.createComment;
import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember;
import static dongwoongkim.springbootboard.factory.post.PostFactory.createPost;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;
    @PersistenceContext EntityManager em;

    Member member;
    Category category;
    Post post;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
        post = postRepository.save(createPost(member,category));
    }

    @DisplayName(value = "댓글 생성 및 조회")
    @Test
    void createAndRead() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        Comment foundComment = commentRepository.findById(comment.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
    }

    @DisplayName(value = "댓글 삭제")
    @Test
    void deleteTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        commentRepository.deleteById(comment.getId());

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }


    @DisplayName(value = "게시글 삭제 -> 댓글 자동 삭제")
    @Test
    void deleteCascadeByPostTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        postRepository.delete(post);
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @DisplayName(value = "부모 댓글 삭제 -> 자식 댓글 삭제")
    @Test
    void deleteCascadeByParentTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        commentRepository.delete(parent);
        clear();

        // then
        commentRepository.findById(child.getId()).isEmpty();
    }

    @DisplayName(value = "자식 댓글 조회")
    @Test
    void getChildrenTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child1 = commentRepository.save(createComment(member, post, parent));
        Comment child2 = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findById(parent.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getChildren().size()).isEqualTo(2);
    }

    @Test
    void findWithParentByIdTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment foundComment = commentRepository.findWithParentById(child.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(foundComment.getParent().getId()).isEqualTo(parent.getId());
    }
    
    @Test
    void deleteCommentTest() {
        // given
        // root 1
        // 1 -> 2
        // 2(del) -> 3(del)
        // 2(del) -> 4
        // 3(del) -> 5

        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment2));
        Comment comment5 = commentRepository.save(createComment(member, post, comment3));

        comment2.delete();
        comment3.delete();
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(comment5.getId()).orElseThrow(CommentNotFoundException::new);
        // 부모 댓글 중 삭제할 수 있는 댓글을 찾고 있으면 삭제, 없으면 comment5.delete()
        comment.findDeletableComment().ifPresentOrElse(c -> commentRepository.delete(c), () -> comment5.delete());
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentsId = comments.stream().map(c -> c.getId()).collect(toList());
        assertThat(comments.size()).isEqualTo(3);
        assertThat(commentsId).contains(comment1.getId(),comment2.getId(),comment4.getId()); // 1, 2, 4
    }

    @Test
    void deleteCommentQueryLogTest() {
        //given
        // 1(del) -> 2(del) -> 3(del) -> 4(del) -> 5
        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment3));
        Comment comment5 = commentRepository.save(createComment(member, post, comment4));

        comment1.delete();
        comment2.delete();
        comment3.delete();
        comment4.delete();
        clear();

        Comment foundComment = commentRepository.findWithParentById(comment5.getId()).orElseThrow(CommentNotFoundException::new);

        // when

        // 부모 댓글 중 삭제할 수 있는 댓글을 찾고 있으면 삭제, 없으면 comment5.delete()로 변경
        Comment comment = commentRepository.findWithParentById(comment5.getId()).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(c -> commentRepository.delete(c), () -> comment5.delete());
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentIds = comments.stream().map(c -> c.getId()).collect(toList());
        assertThat(commentIds.size()).isEqualTo(0);

    }

    @Test
    void findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAscTest() {
        // given
        // 1		NULL
        // 2		1
        // 3		1
        // 4		2
        // 5		2
        // 6		4
        // 7		3
        // 8		NULL

        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment1));
        Comment comment4 = commentRepository.save(createComment(member, post, comment2));
        Comment comment5 = commentRepository.save(createComment(member, post, comment2));
        Comment comment6 = commentRepository.save(createComment(member, post, comment4));
        Comment comment7 = commentRepository.save(createComment(member, post, comment3));
        Comment comment8 = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        List<Comment> result = commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(post.getId());

        // then
        // 1	NULL
        // 8	NULL
        // 2	1
        // 3	1
        // 4	2
        // 5	2
        // 7	3
        // 6	4
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getId()).isEqualTo(comment1.getId());
        assertThat(result.get(1).getId()).isEqualTo(comment8.getId());
        assertThat(result.get(2).getId()).isEqualTo(comment2.getId());
        assertThat(result.get(3).getId()).isEqualTo(comment3.getId());
        assertThat(result.get(4).getId()).isEqualTo(comment4.getId());
        assertThat(result.get(5).getId()).isEqualTo(comment5.getId());
        assertThat(result.get(6).getId()).isEqualTo(comment7.getId());
        assertThat(result.get(7).getId()).isEqualTo(comment6.getId());

    }



    public void clear() {
        em.flush();
        em.clear();
    }
}