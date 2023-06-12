package dongwoongkim.springbootboard.dto.post;

import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.exception.category.CategoryNotFoundException;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.CategoryRepository;
import dongwoongkim.springbootboard.repository.MemberRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ApiModel(value = "게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요", required = true, example = "my title")
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "게시글 내용을 입력해주세요", required = true, example = "my content")
    @NotBlank(message = "게시글 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "상품 가격", notes = "상품 가격을 입력해주세요", required = true, example = "10000")
    @NotBlank(message = "상품 가격을 입력해주세요.")
    private Long price;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "카테고리 아이디", notes = "카테고리 아이디를 입력해주세요", required = true, example = "3")
    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    @PositiveOrZero(message = "올바른 카테고리 아이디를 입력해주세요.")
    private Long categoryId;

    @ApiModelProperty(value = "이미지", notes = "이미지를 첨부해주세요.")
    private List<MultipartFile> images = new ArrayList<>();

    public static Post toEntity(PostCreateRequestDto req, MemberRepository memberRepository, CategoryRepository categoryRepository) {
        return new Post(req.getTitle(),
                req.getContent(),
                req.getPrice(),
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new),
                req.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(Collectors.toList()));
    }

}
