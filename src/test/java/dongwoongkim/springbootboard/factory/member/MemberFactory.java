package dongwoongkim.springbootboard.factory.member;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.role.RoleType;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class MemberFactory {
    public static Member createMember() {
        return new Member("user", "1q2w3e4r!!", "nick", "ee@naver.com", emptyList());
    }
    public static Member createMember2() {
        return new Member("user2", "1q2w3e4r!!2", "nick2", "ee2@naver.com", emptyList());
    }

    public static Member createSendMember() {
        return new Member("sender", "", "sendUser", "e1@naver.com", emptyList());
    }

    public static Member createReceiveMember() {
        return new Member("receiver", "", "receiveUser", "e2@naver.com", emptyList());
    }

    // MemberFactory.java
    public static Member createMemberWithId(Long id) {
        Member member = new Member("email@email.com", "123456a!", "username", "nickname", emptyList());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

}
