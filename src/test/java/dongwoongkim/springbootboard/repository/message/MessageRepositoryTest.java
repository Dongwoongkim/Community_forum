package dongwoongkim.springbootboard.repository.message;

import dongwoongkim.springbootboard.config.querydsl.QuerydslConfig;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.dto.message.MessageSimpleDto;
import dongwoongkim.springbootboard.exception.message.MessageNotFoundException;
import dongwoongkim.springbootboard.factory.member.MemberFactory;
import dongwoongkim.springbootboard.factory.message.MessageFactory;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(QuerydslConfig.class)
class MessageRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MessageRepository messageRepository;

    @PersistenceContext
    EntityManager em;

    Member sender;
    Member receiver;

    @BeforeEach
    void beforeEach() {
        sender = memberRepository.save(MemberFactory.createSendMember());
        receiver = memberRepository.save(MemberFactory.createReceiveMember());
    }

    @Test
    public void createAndReadTest() throws Exception {
        //given
        Message message = messageRepository.save(MessageFactory.createMessage(sender, receiver));
        clear();

        //when
        Message foundMessage = messageRepository.findById(message.getId()).orElseThrow(MessageNotFoundException::new);

        //then
        assertThat(foundMessage.getId()).isEqualTo(message.getId());
    }

    @Test
    public void deleteTest() throws Exception {
        //given
        Message message = messageRepository.save(MessageFactory.createMessage(sender, receiver));
        clear();

        //when
        messageRepository.delete(message);
        clear();

        //then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    public void deleteCascadeBySenderTest() throws Exception {
        //given
        Message message = messageRepository.save(MessageFactory.createMessage(sender, receiver));
        clear();

        //when
        memberRepository.delete(sender);
        clear();

        //then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }
    
    @Test  
    public void deleteCascadeByReceiverTest() throws Exception {
        //given
        Message message = messageRepository.save(MessageFactory.createMessage(sender, receiver));
        clear();

        //when
        memberRepository.delete(receiver);
        clear();

        //then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    public void findWithSenderIdAndReceiverIdTest() throws Exception {
        //given
        Message message = messageRepository.save(MessageFactory.createMessage(sender, receiver));

        //when
        Message foundMessage = messageRepository.findWithSenderAndReceiverById(message.getId()).orElseThrow(MessageNotFoundException::new);

        //then
        assertThat(foundMessage.getReceiveMember().getId()).isEqualTo(receiver.getId());
        assertThat(foundMessage.getSendMember().getId()).isEqualTo(sender.getId());
    }

    // given : 4건 메시지 생성 , 메시지 1개 deleteBySender
    // when : size = 2 , findAllBySenderIdOrderByMessageIdDesc()
    // then : o o x o  -> size가 2이므로 paging 할 때 2건(4,2) 1건(1) 조회되어야 함 (오름차순)
    //        0 1 2 3 In collection
    @Test
    public void findAllBySenderIdOrderByMessageIdDescTest() throws Exception {
        //given
        List<Message> messages = IntStream.range(0, 4).mapToObj(i -> messageRepository.save(MessageFactory.createMessage(sender, receiver))).collect(Collectors.toList());
        messages.get(2).deleteBySender(); // update
        clear();

        //when
        final int size = 2;
        Slice<MessageSimpleDto> result1 = messageRepository.findAllBySenderIdOrderByMessageIdDesc(sender.getId(), Long.MAX_VALUE, PageRequest.ofSize(size));
        List<MessageSimpleDto> content1 = result1.getContent();
        Long lastMessageId1 = content1.get(content1.size() - 1).getId();

        Slice<MessageSimpleDto> result2 = messageRepository.findAllBySenderIdOrderByMessageIdDesc(sender.getId(), lastMessageId1, PageRequest.ofSize(size));
        List<MessageSimpleDto> content2 = result2.getContent();

        //then
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(content1.get(0).getId()).isEqualTo(messages.get(3).getId());
        assertThat(content1.get(1).getId()).isEqualTo(messages.get(1).getId());

        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(content2.get(0).getId()).isEqualTo(messages.get(0).getId());
    }

    @Test
    public void findAllByReceiverIdOrderByMessageIdDescTest() throws Exception {
        //given
        List<Message> messages = IntStream.range(0, 4).mapToObj(i -> messageRepository.save(MessageFactory.createMessage(sender, receiver))).collect(Collectors.toList());
        messages.get(2).deleteByReceiver(); // update
        clear();

        //when
        final int size = 2;
        Slice<MessageSimpleDto> result1 = messageRepository.findAllByReceiverIdOrderByMessageIdDesc(receiver.getId(), Long.MAX_VALUE, PageRequest.ofSize(size));
        List<MessageSimpleDto> content1 = result1.getContent();
        Long lastMessageId1 = content1.get(content1.size() - 1).getId();

        Slice<MessageSimpleDto> result2 = messageRepository.findAllByReceiverIdOrderByMessageIdDesc(receiver.getId(), lastMessageId1, PageRequest.ofSize(size));
        List<MessageSimpleDto> content2 = result2.getContent();

        //then
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(content1.get(0).getId()).isEqualTo(messages.get(3).getId());
        assertThat(content1.get(1).getId()).isEqualTo(messages.get(1).getId());

        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(content2.get(0).getId()).isEqualTo(messages.get(0).getId());
    }

    private void clear() {
        em.flush();
        em.clear();
    }

}