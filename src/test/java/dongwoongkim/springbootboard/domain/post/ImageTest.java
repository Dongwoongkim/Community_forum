package dongwoongkim.springbootboard.domain.post;

import dongwoongkim.springbootboard.exception.image.UnsupportedImagerFormatException;
import dongwoongkim.springbootboard.factory.image.ImageFactory;
import org.junit.jupiter.api.Test;

import static dongwoongkim.springbootboard.factory.image.ImageFactory.createImage;
import static dongwoongkim.springbootboard.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageTest {

    @Test
    void createImageTest() {
        String validExtension = "jpeg";
        ImageFactory.createImageWithOriginName("image." + validExtension);
    }

    @Test
    void createImageUnsupportedFormatTest() {
        String unValidExtension = "unsupported";
        assertThatThrownBy(() -> ImageFactory.createImageWithOriginName("image." + unValidExtension)).isInstanceOf(UnsupportedImagerFormatException.class);
    }

    @Test
    void createImageNonExtensionTest() {
        assertThatThrownBy(() -> ImageFactory.createImageWithOriginName("image")).isInstanceOf(UnsupportedImagerFormatException.class);
    }

    @Test
    void initPostTest() {
        // given
        Image image = createImage();

        // when
        Post post = createPost();
        image.initPost(post);

        // then

        assertThat(image.getPost()).isSameAs(post);
    }

    @Test
    void initPostNotChangedTest() {

        // given
        Image image = createImage();
        image.initPost(createPost());

        // when
        Post post = createPost();
        image.initPost(post);

        // then
        assertThat(image.getPost()).isNotSameAs(post);

    }

}