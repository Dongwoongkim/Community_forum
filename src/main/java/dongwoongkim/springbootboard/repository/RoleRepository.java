package dongwoongkim.springbootboard.repository;

import dongwoongkim.springbootboard.domain.Role;
import dongwoongkim.springbootboard.domain.RoleType;
import org.springframework.boot.json.JsonParser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
