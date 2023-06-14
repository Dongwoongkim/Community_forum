package dongwoongkim.springbootboard.service.post;

import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.post.*;
import dongwoongkim.springbootboard.exception.post.PostNotFoundException;
import dongwoongkim.springbootboard.repository.CategoryRepository;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.PostRepository;
import dongwoongkim.springbootboard.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

        uploadImagesToServer(postCreateRequestDto, post);
        return new PostCreateResponseDto(post.getId());
    }

    public PostReadResponseDto read(Long id) {
         return PostReadResponseDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        deleteImagesFromServer(post);
    }

    @Transactional
    public PostUpdateResponseDto update(Long id, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        PostUpdateResponseDto postUpdateResponseDto = post.updatePost(postUpdateRequestDto);

        uploadImagesToServer(postUpdateRequestDto,postUpdateResponseDto.getImages());

        return postUpdateResponseDto;
    }

    private void uploadImagesToServer(PostCreateRequestDto postCreateRequestDto, Post post) {
        List<Image> images = post.getImages();
        for (int i = 0; i < images.size(); i++) {
            fileService.upload(postCreateRequestDto.getImages().get(i), images.get(i).getUniqueName());
        }
    }
    private void uploadImagesToServer(PostUpdateRequestDto postUpdateRequestDto, List<ImageDto> uploadImages) {
        List<MultipartFile> addImages = postUpdateRequestDto.getAddImages();

        for (int i = 0; i < addImages.size(); i++) {
            fileService.upload(addImages.get(i), uploadImages.get(i).getUniqueName());
        }
    }

    private void deleteImagesFromServer(Post post) {
        List<Image> images = post.getImages();
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
