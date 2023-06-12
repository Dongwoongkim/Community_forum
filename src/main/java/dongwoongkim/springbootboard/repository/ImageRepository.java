package dongwoongkim.springbootboard.repository;

import dongwoongkim.springbootboard.domain.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
