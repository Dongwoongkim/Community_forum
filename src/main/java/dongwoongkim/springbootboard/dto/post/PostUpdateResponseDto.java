package dongwoongkim.springbootboard.dto.post;


import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long price;

    private MemberDto memberResponseDto;

    private List<ImageDto> images;


    public static PostUpdateResponseDto toDto(PostUpdateRequestDto postUpdateRequestDto, Post post) {
        return new PostUpdateResponseDto(post.getId(),
                postUpdateRequestDto.getTitle(),
                postUpdateRequestDto.getContent(),
                postUpdateRequestDto.getPrice(),
                MemberDto.toDto(post.getMember()),
                ImageDto.toDtoList(post.getImages()));
    }


}
