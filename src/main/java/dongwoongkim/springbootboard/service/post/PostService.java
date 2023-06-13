package dongwoongkim.springbootboard.service.post;

import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.post.PostCreateRequestDto;
import dongwoongkim.springbootboard.dto.post.PostCreateResponseDto;
import dongwoongkim.springbootboard.repository.CategoryRepository;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.PostRepository;
import dongwoongkim.springbootboard.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto postCreateRequestDto) {
        Post post = postRepository.save(PostCreateRequestDto.toEntity(postCreateRequestDto, memberRepository, categoryRepository));

        List<Image> images = post.getImages();
        for (Image image : images) {
            log.info("image = {}", image.getOriginName());
        }

        for (int i = 0; i < images.size(); i++) {
            fileService.upload(postCreateRequestDto.getImages().get(i), images.get(i).getUniqueName());
        }
        return new PostCreateResponseDto(post.getId());
    }


}
