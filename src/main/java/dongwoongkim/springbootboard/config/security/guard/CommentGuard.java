package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.exception.comment.CommentNotFoundException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentGuard {
    private final AuthHelper authHelper;
    private final CommentRepository commentRepository;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }
    private boolean hasAuthority(Long id) {
        return authHelper.extractMemberRolesFromContext().contains(RoleType.ADMIN) || isResourceOwner(id);
    }

    private boolean isResourceOwner(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = authHelper.extractMemberId();
        return comment.getMember().getId().equals(memberId);
    }


}