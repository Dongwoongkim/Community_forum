package dongwoongkim.springbootboard.dto.post.image;

import dongwoongkim.springbootboard.domain.post.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String uniqueName;
    private String originName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getUniqueName(), image.getOriginName());
    }

    public static List<ImageDto> toDtoList(List<Image> images) {
        return images.stream().map(i -> ImageDto.toDto(i)).collect(Collectors.toList());
    }
}
