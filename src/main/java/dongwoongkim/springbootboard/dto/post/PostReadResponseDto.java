package dongwoongkim.springbootboard.dto.post;

import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PostReadResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long price;
    private MemberDto memberResponseDto;
    private List<ImageDto> images;

    public static PostReadResponseDto toDto(Post post) {
        return new PostReadResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                MemberDto.toDto(post.getMember()),
                post.getImages().stream().map(it -> ImageDto.toDto(it)).collect(Collectors.toList()));
    }
}
