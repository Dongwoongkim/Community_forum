package dongwoongkim.springbootboard.repository;

import dongwoongkim.springbootboard.domain.role.Role;
import dongwoongkim.springbootboard.domain.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
