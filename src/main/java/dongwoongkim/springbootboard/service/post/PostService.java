package dongwoongkim.springbootboard.service.post;

import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.post.PostCreateRequestDto;
import dongwoongkim.springbootboard.dto.post.PostCreateResponseDto;
import dongwoongkim.springbootboard.dto.post.PostDto;
import dongwoongkim.springbootboard.exception.post.PostNotFoundException;
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

        createImagesToServer(postCreateRequestDto, post);
        return new PostCreateResponseDto(post.getId());
    }

    public PostDto read(Long id) {
         return PostDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        log.info("ID = {}", post.getId());
        postRepository.delete(post);
        deleteImagesFromServer(post);
    }

    private void createImagesToServer(PostCreateRequestDto postCreateRequestDto, Post post) {
        List<Image> images = post.getImages();
        for (int i = 0; i < images.size(); i++) {
            fileService.upload(postCreateRequestDto.getImages().get(i), images.get(i).getUniqueName());
        }
    }

    private void deleteImagesFromServer(Post post) {
        List<Image> images = post.getImages();
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
