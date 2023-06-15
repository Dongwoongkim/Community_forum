package dongwoongkim.springbootboard.domain.post;

import dongwoongkim.springbootboard.dto.post.image.ImageDto;
import dongwoongkim.springbootboard.exception.image.UnsupportedImageFormatException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    private static final String extension[] = {"jpg", "jpeg", "gif", "bmp", "png"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false)
    private String originName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    public Image(String originName) {
        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
    }

    // 양방향 매핑정보
    public void initPost(Post post) {
        if (this.post == null) {
            this.post = post;
        }
    }

    public void cancel(Post post) {
        if (this.post != null) {
            this.post = null;
        }
    }

    private String generateUniqueName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String extractExtension(String originName) {
        try {
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if (supportFormat(ext)) {
                return ext;
            }
        } catch (StringIndexOutOfBoundsException e) {}
        throw new UnsupportedImageFormatException();
    }

    private boolean supportFormat(String ext) {
        return Arrays.stream(extension).anyMatch(e -> e.equalsIgnoreCase(ext));
    }

    public static Image toEntity(ImageDto imageDto) {
        return new Image(imageDto.getOriginName());
    }
}
