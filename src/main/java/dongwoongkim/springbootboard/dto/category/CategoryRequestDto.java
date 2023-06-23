package dongwoongkim.springbootboard.dto.category;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.repository.category.CategoryRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@ApiModel(value = "카테고리 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    @ApiModelProperty(value = "카테고리 명", notes = "카테고리명을 입력해주세요.", required = true, example = "Book")
    @NotBlank(message = "카테고리명은 반드시 입력하셔야 합니다.")
    private String name;

    @ApiModelProperty(value = "부모 카테고리 아이디" , notes = "부모 카테고리 아이디를 입력해주세요.", example = "2")
    private Long parentId;

    public static Category toEntity(CategoryRequestDto categoryRequestDto, CategoryRepository categoryRepository) {
        if (categoryRequestDto.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryRequestDto.getParentId()).orElse(null);
            if (parentCategory != null) {
                return new Category(categoryRequestDto.getName(), parentCategory);
            }
        }
        return new Category(categoryRequestDto.getName(), null);
    }
}
