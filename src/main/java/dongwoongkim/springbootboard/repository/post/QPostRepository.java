package dongwoongkim.springbootboard.repository.post;

import dongwoongkim.springbootboard.dto.post.read.condition.PostDto;
import dongwoongkim.springbootboard.dto.post.read.condition.PostReadCondition;
import org.springframework.data.domain.Page;

public interface QPostRepository {
    Page<PostDto> findAllByCondition(PostReadCondition cond);
}
