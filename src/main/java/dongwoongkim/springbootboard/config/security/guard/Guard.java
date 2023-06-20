package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.helper.AuthHelper;

public abstract class Guard {
    public final boolean check(Long id) {
        return AuthHelper.isAuthenticated() && hasAuthority(id);
    }
    private boolean hasAuthority(Long id) {
        return AuthHelper.extractMemberRolesFromContext().contains(RoleType.ADMIN) || isResourceOwner(id);
    }

    abstract protected boolean isResourceOwner(Long id);


}
