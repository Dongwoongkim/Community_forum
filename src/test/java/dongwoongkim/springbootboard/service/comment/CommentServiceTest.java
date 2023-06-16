package dongwoongkim.springbootboard.service.comment;

import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    @InjectMocks CommentService commentService;
    @Mock CommentRepository commentRepository;
    @Mock MemberRepository memberRepository;
    @Mock PostRepository postRepository;

    @Test
    void readAllTest() {
        // given

        // when

        // then
    }

    @Test
    void readAllDeletedCommentTest() {

    }

    @Test
    void createTest (){

    }

    @Test
    void createExceptionByMemberNotFoundTest() {

    }

    @Test
    void createExceptionByPostNotFoundTest() {

    }

    @Test
    void createExceptionByCommentNotFoundTest() {

    }

}