package dongwoongkim.springbootboard.config.security.guard;

import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.auth.AccessDeniedException;
import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostGuard extends Guard{
    private final PostRepository postRepository;

    @Override
    protected boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = AuthHelper.extractMemberId();
        return post.getMember().getId().equals(memberId);
    }
}
