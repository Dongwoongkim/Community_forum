package dongwoongkim.springbootboard.dto.member;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.member.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;

    public static MemberDto toDto(Member member) {
        return new MemberDto(
                member.getId(),
                member.getUsername(),
                member.getNickname(),
                member.getEmail());
    }
    public static MemberDto empty() {
        return new MemberDto(null, "", "", "");
    }
}
