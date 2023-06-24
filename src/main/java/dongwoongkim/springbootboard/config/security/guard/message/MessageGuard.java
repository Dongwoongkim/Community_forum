package dongwoongkim.springbootboard.config.security.guard.message;

import dongwoongkim.springbootboard.config.security.guard.Guard;
import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageGuard extends Guard {
    
    private final MessageRepository messageRepository;
    
    @Override
    protected boolean isResourceOwner(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long receiverId = message.getReceiveMember().getId();
        Long senderId = message.getSendMember().getId();
        Long memberId = AuthHelper.extractMemberId();

        return memberId.equals(receiverId) || memberId.equals(senderId);
    }
}
