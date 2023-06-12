package dongwoongkim.springbootboard.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


class LocalFileServiceTest {
    LocalFileService localFileService = new LocalFileService();
    String testLocation = new File("src/main/resources/static").getAbsolutePath() + "/";

    @BeforeEach
    void beforeEach() throws IOException { // 2
        ReflectionTestUtils.setField(localFileService, "location", testLocation); // localService의 location에 testLocation 주입
        System.out.println("testLocation = " + testLocation);
    }

    @AfterEach
    void afterEach() throws IOException {
        FileUtils.cleanDirectory(new File(testLocation));
    }

    @Test
    void uploadTest() throws IOException {
        // given
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String fileName = "testFile.txt";

        // when
        localFileService.upload(file,fileName);

        // then
        boolean exists = new File(testLocation + fileName).exists();
        Assertions.assertThat(exists).isTrue();
    }


}