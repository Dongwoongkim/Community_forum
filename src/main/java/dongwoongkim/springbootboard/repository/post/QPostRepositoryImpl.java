package dongwoongkim.springbootboard.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.post.read.condition.PostDto;
import dongwoongkim.springbootboard.dto.post.read.condition.PostReadCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static dongwoongkim.springbootboard.domain.post.QPost.post;

@Slf4j
@Repository
@Transactional(readOnly = true)
public class QPostRepositoryImpl extends QuerydslRepositorySupport implements QPostRepository {

    private final JPAQueryFactory query;

    public QPostRepositoryImpl(JPAQueryFactory query) {
        super(Post.class);
        this.query = query;
    }

    @Override
    public Page<PostDto> findAllByCondition(PostReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private List<PostDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                query
                        .select(constructor(PostDto.class,
                                post.id,
                                post.title,
                                post.member.nickname,
                                post.createdTime))
                        .from(post)
                        .join(post.member)
                        .where(predicate)
                        .orderBy(post.id.desc())
        ).fetch();
    }
    private Long fetchCount(Predicate predicate) { // 7
        return query.select(post.count()).from(post).where(predicate).fetchOne();
    }


    private Predicate createPredicate(PostReadCondition cond) {
        return new BooleanBuilder()
                .and(orConditionsByEqCategoryIds(cond.getCategoryId()))
                .and(orConditionsByEqMemberIds(cond.getMemberId()));
    }

    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) {
        return orConditions(memberIds, post.member.id::eq);
    }

    private Predicate orConditionsByEqCategoryIds(List<Long> categoryIds) {
        return orConditions(categoryIds, post.category.id::eq);
    }

    private Predicate orConditions(List<Long> values, Function<Long,BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

}
