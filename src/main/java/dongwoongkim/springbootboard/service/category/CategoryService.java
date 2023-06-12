package dongwoongkim.springbootboard.service.category;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.dto.category.CategoryRequestDto;
import dongwoongkim.springbootboard.dto.category.CategoryResponseDto;
import dongwoongkim.springbootboard.exception.category.CategoryNotFoundException;
import dongwoongkim.springbootboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> readAll() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryResponseDto.toDtoList(categories);
    }

    @Transactional
    public void create(CategoryRequestDto categoryRequestDto) {
        categoryRepository.save(CategoryRequestDto.toEntity(categoryRequestDto, categoryRepository));
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new));
    }
}
