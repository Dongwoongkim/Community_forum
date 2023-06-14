package dongwoongkim.springbootboard.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "게시글 수정 결과")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {

    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요", required = true, example = "my title")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "게시글 내용 입력해주세요", required = true, example = "my content")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "가격", notes = "가격을 입력해주세요", required = true, example = "10000")
    @NotNull
    @PositiveOrZero
    private Long price;

    @ApiModelProperty(value = "추가 이미지", notes = "추가될 이미지 첨부")
    private List<MultipartFile> addImages = new ArrayList<>();

    @ApiModelProperty(value = "삭제할 이미지", notes = "삭제할 이미지 ID")
    private List<Long> deleteImagesIds = new ArrayList<>();

}
