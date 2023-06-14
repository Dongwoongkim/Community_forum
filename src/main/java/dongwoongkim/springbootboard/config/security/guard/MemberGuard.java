package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard {

    private final AuthHelper authHelper;
    public boolean check(Long id) {
        log.info("id = {}", id);
        log.info("a = {}", authHelper.isAuthenticated());
        log.info("b = {}", hasAuthority(id));

        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        Long memberId = authHelper.extractMemberId();
        if (memberId == null) {
            return false;
        }
        List<RoleType> roleTypes = authHelper.extractMemberRolesFromContext();
        return id.equals(memberId) || roleTypes.contains(RoleType.ADMIN);
    }
}
