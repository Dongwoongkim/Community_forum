package dongwoongkim.springbootboard.dto.post.read;

import dongwoongkim.springbootboard.dto.post.read.condition.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Data
@AllArgsConstructor
public class PostListPagedDto {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<PostDto> postList;

    public static PostListPagedDto toDto(Page<PostDto> page) {
        return new PostListPagedDto(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent()
        );
    }

}
