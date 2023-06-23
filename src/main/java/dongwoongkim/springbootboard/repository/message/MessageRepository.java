package dongwoongkim.springbootboard.repository.message;

import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.dto.message.MessageSimpleDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 수신 및 송신 메시지 전체 조회
    @Query("select m from Message m left join fetch m.sendMember left join fetch m.receiveMember where m.id =:id")
    Optional<Message> findWithSenderAndReceiverById(@Param("id") Long id);

    // 송신 내역 조회 ( 보낸 사람 - 받은 사람 )
    @Query("select new dongwoongkim.springbootboard.dto.message.MessageSimpleDto(m.id, m.receiveMember.nickname, m.content, m.createdTime)" +
            " from Message m left join m.receiveMember" +
            " where m.sendMember.id = :senderId and m.id < :lastMessageId and m.deletedBySender = false" +
            " order by m.id desc")
    Slice<MessageSimpleDto> findAllBySenderIdOrderByMessageIdDesc(@Param("senderId") Long senderId,
                                                                  @Param("lastMessageId") Long lastMessageId, Pageable pageable);

    // 수신 내역 조회 ( 받은 사람 - 보낸 사람 )
    @Query("select new dongwoongkim.springbootboard.dto.message.MessageSimpleDto(m.id, m.sendMember.nickname, m.content, m.createdTime)" +
            " from Message m left join m.sendMember" +
            " where m.receiveMember.id = :receiverId and m.id < :lastMessageId and m.deletedByReceiver = false" +
            " order by m.id desc")
    Slice<MessageSimpleDto> findAllByReceiverIdOrderByMessageIdDesc(@Param("receiverId") Long receiverId,
                                                           @Param("lastMessageId") Long lastMessageId, Pageable pageable);

}
