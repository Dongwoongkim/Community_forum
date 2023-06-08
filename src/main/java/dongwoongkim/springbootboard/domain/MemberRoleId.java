package dongwoongkim.springbootboard.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class MemberRoleId implements Serializable {
    private Member member;
    private Role role;
}
