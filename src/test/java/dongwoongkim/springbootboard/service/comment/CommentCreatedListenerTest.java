package dongwoongkim.springbootboard.service.comment;

import dongwoongkim.springbootboard.dto.alarm.AlarmInfoDto;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import dongwoongkim.springbootboard.event.comment.CommentCreatedEvent;
import dongwoongkim.springbootboard.factory.member.MemberFactory;
import dongwoongkim.springbootboard.service.alarm.AlarmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMemberWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
@Commit
class CommentCreatedListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;
    @MockBean(name = "smsAlarmService") AlarmService smsAlarmService; // 4
    @MockBean(name = "emailAlarmService") AlarmService emailAlarmService; // 4
    @MockBean(name = "kakaoAlarmService") AlarmService kakaoAlarmService; // 4

    int calledCount;

    @AfterTransaction
    void afterEach() {
        verify(emailAlarmService,times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(kakaoAlarmService,times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(smsAlarmService,times(calledCount)).alarm(any(AlarmInfoDto.class));
    }

    @Test
    public void handleCommentCreatedEventTest() throws Exception {
        //given
        MemberDto poster = MemberDto.toDto(createMemberWithId(1L));
        MemberDto parentWriter = MemberDto.toDto(createMemberWithId(2L));
        MemberDto commentWriter = MemberDto.toDto(createMemberWithId(3L));
        String content = "content";

        //when
        this.publisher.publishEvent(new CommentCreatedEvent(commentWriter, parentWriter, poster, content));

        //then
        calledCount = 2;
    }
}