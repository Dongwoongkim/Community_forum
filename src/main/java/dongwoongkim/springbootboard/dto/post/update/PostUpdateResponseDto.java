package dongwoongkim.springbootboard.dto.post.update;


import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import dongwoongkim.springbootboard.dto.post.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private List<ImageDto> addedImages;
    private List<ImageDto> deletedImages;


    public static PostUpdateResponseDto toDto(PostUpdateRequestDto postUpdateRequestDto, Post post, Post.UpdatedImageResult updatedImageResult) {
        return new PostUpdateResponseDto(post.getId(),
                postUpdateRequestDto.getTitle(),
                postUpdateRequestDto.getContent(),
                postUpdateRequestDto.getPrice(),
                MemberDto.toDto(post.getMember()),
                ImageDto.toDtoList(updatedImageResult.getAddImagesList()),
                ImageDto.toDtoList(updatedImageResult.getDeleteImageList()));
    }


}
