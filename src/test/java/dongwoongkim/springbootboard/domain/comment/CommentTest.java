package dongwoongkim.springbootboard.domain.comment;

import dongwoongkim.springbootboard.factory.comment.CommentFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static dongwoongkim.springbootboard.factory.comment.CommentFactory.createComment;
import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember;
import static dongwoongkim.springbootboard.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void deleteTest() {
        // given
        Comment comment = createComment(null);
        boolean beforeDeleted = comment.isDeleted();

        // when
        comment.delete();

        // then
        assertThat(beforeDeleted).isFalse();
        assertThat(comment.isDeleted()).isTrue();
    }

    @Test
    void findDeletableCommentWhenExistTest() {
        // given
        //  root 1
        //  1 -> 2
        //  2(del) -> 3(del)
        //  2(del) -> 4
        //  3(del) -> 5

        Comment comment1 = createComment(null);
        Comment comment2 = createComment(comment1);
        Comment comment3 = createComment(comment2);
        Comment comment4 = createComment(comment2);
        Comment comment5 = createComment(comment3);

        comment2.delete();
        comment3.delete();

        ReflectionTestUtils.setField(comment1, "children", List.of(comment2));
        ReflectionTestUtils.setField(comment2, "children", List.of(comment3, comment4));
        ReflectionTestUtils.setField(comment3, "children", List.of(comment5));
        ReflectionTestUtils.setField(comment4, "children", List.of());
        ReflectionTestUtils.setField(comment5, "children", List.of());

        Optional<Comment> deletableComment = comment5.findDeletableComment();
        assertThat(deletableComment).containsSame(comment3);
    }

    @Test
    void findDeletableCommentWhenNotExistsTest() {
        Comment comment1 = createComment(null);
        Comment comment2 = createComment(comment1);
        Comment comment3 = createComment(comment2);
        Comment comment4 = createComment(comment2);
        Comment comment5 = createComment(comment3);

        comment2.delete();
        comment3.delete();

        ReflectionTestUtils.setField(comment1, "children", List.of(comment2));
        ReflectionTestUtils.setField(comment2, "children", List.of(comment3, comment4));
        ReflectionTestUtils.setField(comment3, "children", List.of(comment5));
        ReflectionTestUtils.setField(comment4, "children", List.of());
        ReflectionTestUtils.setField(comment5, "children", List.of());

        Optional<Comment> deletableComment = comment3.findDeletableComment();
        assertThat(deletableComment).isEmpty();

    }


}