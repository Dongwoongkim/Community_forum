package dongwoongkim.springbootboard.encoder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void encode() {
        System.out.println(passwordEncoder.getClass());
    }
}
