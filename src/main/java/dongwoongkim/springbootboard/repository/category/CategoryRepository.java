package dongwoongkim.springbootboard.repository.category;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.dto.post.read.condition.PostDto;
import dongwoongkim.springbootboard.dto.post.read.condition.PostReadCondition;
import dongwoongkim.springbootboard.repository.post.QPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c left join c.parent p order by p.id asc nulls first, c.id asc")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();

}
