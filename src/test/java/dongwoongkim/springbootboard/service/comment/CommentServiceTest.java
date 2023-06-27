package dongwoongkim.springbootboard.service.comment;

import dongwoongkim.springbootboard.event.comment.CommentCreatedEvent;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static dongwoongkim.springbootboard.factory.comment.CommentFactory.commentCreateRequestDto;
import static dongwoongkim.springbootboard.factory.comment.CommentFactory.createComment;
import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember;
import static dongwoongkim.springbootboard.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    ApplicationEventPublisher publisher;

    @Test
    public void createTest() throws Exception {
        //given
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember())); // 행동 정의
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost())); // 행동 정의
        given(commentRepository.save(any())).willReturn(createComment(null)); // 행동 정의

        //when
        commentService.create(commentCreateRequestDto());

        //then
        verify(commentRepository).save(any());
        verify(publisher).publishEvent(eventCaptor.capture());
        Object event = eventCaptor.getValue();
        assertThat(event).isInstanceOf(CommentCreatedEvent.class);
    }

}