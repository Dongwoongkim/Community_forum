package dongwoongkim.springbootboard.domain.member;

import dongwoongkim.springbootboard.domain.role.Role;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class MemberRoleId implements Serializable {
    private Member member;
    private Role role;
}
