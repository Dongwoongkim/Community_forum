package dongwoongkim.springbootboard.domain.member;

import dongwoongkim.springbootboard.domain.BaseEntity;
import dongwoongkim.springbootboard.domain.role.Role;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Slf4j
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberRole> roles;

    public Member(String username, String password, String nickname, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        List<MemberRole> roleList = roles.stream().map(role -> new MemberRole(this,role)).collect(Collectors.toList());
        this.roles = roleList;
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }
    public void updateEmail(String email) {
        this.email = email;
    }
    public void updatePassword(String password) {
        this.password = password;
    }
}
