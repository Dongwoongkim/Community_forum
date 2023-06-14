package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.exception.auth.AuthenticationEntryPointException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostGuard {
    private final AuthHelper authHelper;
    private final PostRepository postRepository;

    public boolean check(Long postId) {
        return authHelper.isAuthenticated() && hasAuthority(postId);
    }

    private boolean hasAuthority(Long postId) {
        return authHelper.extractMemberRolesFromContext().contains(RoleType.ADMIN) || isResourceOwner(postId);
    }

    private boolean isResourceOwner(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(AccessDeniedException::new);
        Long memberId = authHelper.extractMemberId();

        return post.getMember().getId().equals(memberId);
    }


}
