package dongwoongkim.springbootboard.domain.message;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import dongwoongkim.springbootboard.domain.base.BaseEntity;
import dongwoongkim.springbootboard.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean deletedBySender;


    @Column(nullable = false)
    private boolean deletedByReceiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member sendMember;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiveMember;

    public Message(String content, Member sendMember, Member receiveMember) {
        this.content = content;
        this.sendMember = sendMember;
        this.receiveMember = receiveMember;

        this.deletedByReceiver = false;
        this.deletedBySender = false;
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeletable() {
        return isDeletedByReceiver() && isDeletedBySender();
    }

}
