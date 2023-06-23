package dongwoongkim.springbootboard.service.message;

import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.dto.message.*;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.exception.message.MessageNotFoundException;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;


    @Transactional
    public void create(MessageCreateRequestDto messageCreateRequestDto) {
        messageRepository.save(
                MessageCreateRequestDto.toEntity(messageCreateRequestDto, memberRepository));
    }

    @Transactional
    public void deleteBySender(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        message.deleteBySender();
        if (message.isDeletable()) {
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteByReceiver(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        message.deleteByReceiver();
        if (message.isDeletable()) {
            messageRepository.delete(message);
        }
    }


    // 단건 조회
    public MessageDto read(Long id) {
        return MessageDto.toDto(messageRepository.findWithSenderAndReceiverById(id).orElseThrow(MessageNotFoundException::new));
    }


    // 보낸 메시지 조회
    public MessageDtoList readAllSendMessage(MessageReadCondition cond) {
        return MessageDtoList.toDto(
                messageRepository.findAllBySenderIdOrderByMessageIdDesc(
                        cond.getMemberId(),
                        cond.getLastMessageId(),
                        Pageable.ofSize(cond.getSize())
                )
        );
    }

    // 받은 메시지 조회
    public MessageDtoList readAllReceiveMessage(MessageReadCondition cond) {
        return MessageDtoList.toDto(
                messageRepository.findAllByReceiverIdOrderByMessageIdDesc(
                        cond.getMemberId(),
                        cond.getLastMessageId(),
                        Pageable.ofSize(cond.getSize())
                )
        );
    }

}
