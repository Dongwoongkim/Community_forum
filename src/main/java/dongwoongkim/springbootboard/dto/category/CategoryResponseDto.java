package dongwoongkim.springbootboard.dto.category;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CategoryResponseDto {

    private Long id;
    private String name;
    private List<CategoryResponseDto> children;

    public static List<CategoryResponseDto> toDtoList(List<Category> categories) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                categories,
                c -> new CategoryResponseDto(c.getId(), c.getName(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                dto -> dto.getChildren()
        );

        return helper.convert();
    }
}
