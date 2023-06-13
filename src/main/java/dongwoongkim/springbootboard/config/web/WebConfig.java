package dongwoongkim.springbootboard.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.image.location}")
    private String location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**") // 1
                .addResourceLocations("file:" + location) // 2
                .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic()); //
        // 1번 요청에 대해 2번 위치로 이동
        // 자원 접근할때마다 새로운 자원을 내려받지 않고, 캐시된 자원 이용
    }
}
