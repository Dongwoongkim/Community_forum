package dongwoongkim.springbootboard.service.comment;

import dongwoongkim.springbootboard.dto.alarm.AlarmInfoDto;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import dongwoongkim.springbootboard.event.comment.CommentCreatedEvent;
import dongwoongkim.springbootboard.service.alarm.AlarmService;
import dongwoongkim.springbootboard.service.alarm.KakaoAlarmService;
import dongwoongkim.springbootboard.service.alarm.SmsAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentCreatedListener {

    private final AlarmService emailAlarmService;
    private final SmsAlarmService smsAlarmService;
    private final KakaoAlarmService kakaoAlarmService;
    private List<AlarmService> alarmServices = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        alarmServices.add(emailAlarmService);
        alarmServices.add(smsAlarmService);
        alarmServices.add(kakaoAlarmService);
    }

    /**
     * @TransactionalEventListner : 이벤트를 발행하던 트랜잭션의 흐름에 따라, 이벤트 제어 (커밋 이후 이벤트 수행하도록 설정)
     * @Async : 이벤트 처리를 새로운 스레드에서 비동기적으로 처리하여 응답 지연 문제 해결
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 트랜잭션 커밋된 이후 이벤트 수행하도록 설정
    @Async // 비동기 처리 (다른 스레드에서 실행
    public void handleAlarm(CommentCreatedEvent event) {
        log.info("CommentCreatedEvent.handleAlarm");
        String message = generateAlarmMessage(event);

        if (isAbleToSendToParentWriter(event)) {
            alarmServices.stream().forEach(alarmService -> alarmService.alarm(new AlarmInfoDto(event.getPostWriter(), message)));
        }
        if (isAbleToSendToPostWriter(event)) {
            alarmServices.stream().forEach(alarmService -> alarmService.alarm(new AlarmInfoDto(event.getPostWriter(), message)));
        }

    }

    private boolean isAbleToSendToPostWriter(CommentCreatedEvent event) {
        // 글쓴이와 댓글 작성자가 동일하지 않을 때, 즉 댓글을 쓴사람이 글쓴이가 아닐 때 이벤트 발생
        if(!isSameMember(event.getPublisher(), event.getPostWriter())) {

            // 만약 해당 댓글의 부모댓글이 있으면 부모 댓글과, 글쓴이 비교해서 같지 않을때 true 리턴
            if(hasParent(event)) return !isSameMember(event.getPostWriter(), event.getParentPublisher());

            return true;
        }
        return false;
    }

    private boolean isAbleToSendToParentWriter(CommentCreatedEvent event) {
        // 상위 댓글이 있고, 상위댓글 작성자와 하위댓글 작성자가 동일하지 않을 때 이벤트 발생
        return hasParent(event) && !isSameMember(event.getPublisher(), event.getParentPublisher());
    }


    private boolean isSameMember(MemberDto a, MemberDto b) {
        return Objects.equals(a.getId(), b.getId());
    }

    private boolean hasParent(CommentCreatedEvent event) {
        return event.getParentPublisher().getId() != null;
    }
    private String generateAlarmMessage(CommentCreatedEvent event) {
        return event.getPublisher().getNickname() + " : " + event.getContent();
    }

}
