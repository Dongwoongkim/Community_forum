package dongwoongkim.springbootboard.factory.message;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.message.Message;

import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember;
import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember2;

public class MessageFactory {

    public static Message createMessage() {
        return new Message("content", createMember(), createMember2());
    }

    public static Message createMessage(Member sender, Member receiver) {
        return new Message("content", sender, receiver);
    }

}
