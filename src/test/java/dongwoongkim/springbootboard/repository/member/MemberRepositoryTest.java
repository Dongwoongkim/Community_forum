package dongwoongkim.springbootboard.repository.member;

import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.member.MemberRole;
import dongwoongkim.springbootboard.domain.role.Role;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@DataJpaTest
class MemberRepositoryTest {

    @PersistenceContext EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;

    private void clear() {
        em.flush();
        em.clear();
    }

    @Test
    void create() {
        // given
        Member member = createMember();
        // when
        memberRepository.save(member);
        // then
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void existsByEmailTest() {
        Member member = memberRepository.save(createMember());
        Assertions.assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
    }

    @Test
    void existsByUsername() {
        Member member = memberRepository.save(createMember());
        Assertions.assertThat(memberRepository.existsByUsername(member.getUsername())).isTrue();
    }

    @Test
    void findAllByNickname() {
        Member member = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());
        Assertions.assertThat(memberRepository.findAllByNickname(member.getNickname()).stream().count()).isEqualTo(2);
    }

    @Test
    void memberRoleCascadeCreateTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.USER, RoleType.MANAGER);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        // when
        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();
        List<MemberRole> result = em.createQuery("select mr from MemberRole mr", MemberRole.class).getResultList();

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(member.getRoles().size()).isEqualTo(2);
    }

    @Test
    void memberRoleCascadeDeleteTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.USER, RoleType.ADMIN, RoleType.MANAGER);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        Assertions.assertThat(memberRepository.findAll().size()).isZero();

        // then
        List<MemberRole> result = em.createQuery("select mr from MemberRole mr", MemberRole.class).getResultList();
        Assertions.assertThat(result.size()).isZero();
    }

    private Member createMemberWithRoles(List<Role> roles) {
        return new Member("email", "password", "username", "nickname", roles);
    }

    private Member createMember() {
        return new Member("kim","qwer1234","do","rla@naver.com", emptyList());
    }

    private Member createMember(String username, String password, String nickname, String email) {
        return new Member(username, password, nickname, email, emptyList());
    }

}