package dongwoongkim.springbootboard.dto.post;

import dongwoongkim.springbootboard.domain.post.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String uniqueName;
    private String originName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName());
    }

}
