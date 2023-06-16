package dongwoongkim.springbootboard.repository.comment;

import dongwoongkim.springbootboard.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    // 댓글로 조회하면서 자신의 부모 댓글까지 함께 조회
    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    Optional<Comment> findWithParentById(@Param("id") Long id);

    // 모든 댓글을 조회해서 부모의 아이디로 NULL 우선 오름차순 정렬
    // 그 다음으로 자신의 아이디로 오름차순 정렬 조회
    @Query("select c from Comment c join fetch c.member left join fetch c.parent where c.post.id = :postId" +
            " order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(@Param("postId") Long postId);



}
