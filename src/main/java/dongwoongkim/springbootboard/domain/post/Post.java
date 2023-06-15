package dongwoongkim.springbootboard.domain.post;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.dto.post.PostUpdateRequestDto;
import dongwoongkim.springbootboard.dto.post.PostUpdateResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // member 엔티티 사라지면 Post Table에 member_id row 모두 삭제
    // @OnDelete 대신 Member Entity에 Post를 걸어주어 @OneToMany(cascade = all) 로 지정하는 방법도 있지만,
    // 굳이 Member에 Post를 양방향매핑을 만들어줄 필요가 없다고 판단
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // member 엔티티 사라지면 Category Table에 member_id row 모두 삭제
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    public Post(String title, String content, Long price, Member member, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }

    public PostUpdateResponseDto updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        this.title = postUpdateRequestDto.getTitle();
        this.content = postUpdateRequestDto.getContent();
        this.price = postUpdateRequestDto.getPrice();

        List<Image> addImages = ImageAddList(postUpdateRequestDto.getAddImages());
        List<Image> deleteImages = convertIdsToImage(postUpdateRequestDto.getDeleteImageIds());

        UpdatedImageResult updatedImageResult = new UpdatedImageResult(addImages, deleteImages);
        update(addImages, deleteImages); // add & delete

        return PostUpdateResponseDto.toDto(postUpdateRequestDto,this, updatedImageResult);
    }

    private void update(List<Image> addImageList, List<Image> deleteImageList) {
        addImages(addImageList);
        deleteImages(deleteImageList);
    }

    private List<Image> ImageAddList(List<MultipartFile> addImages) {
        return addImages.stream().map(mf -> new Image(mf.getOriginalFilename())).collect(Collectors.toList());
    }

    private void addImages(List<Image> addList) {
        addList.stream().forEach(
                i -> {
                    images.add(i);
                    i.initPost(this);
                });
    }

    private void deleteImages(List<Image> deleteList) {
        deleteList.stream().forEach(
                i -> {
                    images.remove(i);
                }
        );
    }

    private List<Image> convertIdsToImage(List<Long> imageIdList) {
        return imageIdList.stream().map(id -> convertIdToImage(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(Collectors.toList());
    }

    private Optional<Image> convertIdToImage(Long id) {
        return this.images.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    @AllArgsConstructor
    @Getter
    public class UpdatedImageResult {
        private List<Image> addImagesList;
        private List<Image> deleteImageList;
    }
}
