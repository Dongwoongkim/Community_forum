package dongwoongkim.springbootboard.config.security.guard.message;

import dongwoongkim.springbootboard.config.security.guard.Guard;
import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiveGuard extends Guard {
    private final MessageRepository messageRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Message message = messageRepository.findWithSenderAndReceiverById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = AuthHelper.extractMemberId();
        Long receiveId = message.getReceiveMember().getId();

        return memberId.equals(receiveId);
    }
}
