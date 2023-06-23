package dongwoongkim.springbootboard.service.post;

import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.dto.post.create.PostCreateRequestDto;
import dongwoongkim.springbootboard.dto.post.create.PostCreateResponseDto;
import dongwoongkim.springbootboard.dto.post.image.ImageDto;
import dongwoongkim.springbootboard.dto.post.read.PostListPagedDto;
import dongwoongkim.springbootboard.dto.post.read.PostReadResponseDto;
import dongwoongkim.springbootboard.dto.post.read.condition.PostReadCondition;
import dongwoongkim.springbootboard.dto.post.update.PostUpdateRequestDto;
import dongwoongkim.springbootboard.dto.post.update.PostUpdateResponseDto;
import dongwoongkim.springbootboard.exception.post.PostNotFoundException;
import dongwoongkim.springbootboard.repository.category.CategoryRepository;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
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

        uploadImagesToServer(postCreateRequestDto, post);
        return new PostCreateResponseDto(post.getId());
    }

    public PostReadResponseDto read(Long id) {
         return PostReadResponseDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    public PostListPagedDto readAll(PostReadCondition condition) {
        return PostListPagedDto.toDto(postRepository.findAllByCondition(condition));
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

        uploadImagesToServer(postUpdateRequestDto, postUpdateResponseDto);
        deleteImagesFromServer(postUpdateResponseDto);

        return postUpdateResponseDto;
    }

    private void uploadImagesToServer(PostCreateRequestDto postCreateRequestDto, Post post) {
        List<Image> images = post.getImages();

        for (int i = 0; i < images.size(); i++) {
            fileService.upload(postCreateRequestDto.getImages().get(i), images.get(i).getUniqueName());
        }
    }

    private void deleteImagesFromServer(Post post) {
        List<Image> images = post.getImages();
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

    private void uploadImagesToServer(PostUpdateRequestDto postUpdateRequestDto, PostUpdateResponseDto postUpdateResponseDto) {
        List<ImageDto> addedImages = postUpdateResponseDto.getAddedImages();
        for (int i = 0; i < addedImages.size(); i++) {
            fileService.upload(postUpdateRequestDto.getAddImages().get(i), addedImages.get(i).getUniqueName());
        }
    }

    private void deleteImagesFromServer(PostUpdateResponseDto postUpdateResponseDto) {
        List<ImageDto> deleteImageDtoList = postUpdateResponseDto.getDeletedImages();
        deleteImageDtoList.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

}
