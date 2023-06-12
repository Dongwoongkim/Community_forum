package dongwoongkim.springbootboard.service.file;

import dongwoongkim.springbootboard.exception.image.FileUploadFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class LocalFileService implements FileService{

    @Value("${upload.image.location}")
    private String location; // 파일을 업로드할 위치 주입

    @PostConstruct
    void postConstruct() { // 파일 업로드 될 디렉토리가 없을경우 생성
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @Override
    public void upload(MultipartFile file, String filename) { // MultipartFile을 실제 파일로 지정된 위치에 저장.
        try {
            file.transferTo(new File(location + filename)); // transferTo를 통해 파일을 서버에 저장
            // 서버 저장시 UUID + . + 확장자
        } catch (IOException e) {
            throw new FileUploadFailureException();
        }
    }

    @Override
    public void delete(String filename) {

    }
}
