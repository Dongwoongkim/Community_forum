package dongwoongkim.springbootboard.factory.image;

import dongwoongkim.springbootboard.domain.post.Image;

public class ImageFactory {

    public static Image createImageWithOriginName(String originName) {
        return new Image(originName);
    }

    public static Image createImage() {
        return new Image("origin_filename.jpg");
    }
}
