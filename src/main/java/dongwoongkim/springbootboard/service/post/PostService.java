package dongwoongkim.springbootboard.service.post;

import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.post.PostRequestDto;
import dongwoongkim.springbootboard.repository.CategoryRepository;
import dongwoongkim.springbootboard.repository.ImageRepository;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.PostRepository;
import dongwoongkim.springbootboard.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public void create(PostRequestDto postRequestDto) {
        Post post = postRepository.save(PostRequestDto.toEntity(postRequestDto, memberRepository, categoryRepository));
        uploadImages(post.getImages(), postRequestDto.getImages());
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }
}

