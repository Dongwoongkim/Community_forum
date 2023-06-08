package dongwoongkim.springbootboard.repository;

import dongwoongkim.springbootboard.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Member> findOneWithRolesByUsername(String username);
    Optional<Member> findByUsername(String username);
    List<Member> findAllByNickname(String nickname);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}