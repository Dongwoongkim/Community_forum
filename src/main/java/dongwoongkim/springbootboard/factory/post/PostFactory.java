package dongwoongkim.springbootboard.factory.post;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.post.Image;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.factory.category.CategoryFactory;
import dongwoongkim.springbootboard.factory.member.MemberFactory;

import java.util.List;

import static dongwoongkim.springbootboard.factory.category.CategoryFactory.createCategory;
import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember;

public class PostFactory {

    public static Post createPost() {
        return new Post("title", "content", 1000L, createMember(), createCategory(), List.of());
    }
    public static Post createPost(Member member, Category category) {
        return new Post("title", "content", 1000L, member, category, List.of());
    }

    public static Post createPostWithImages(List<Image> images) {
        return new Post("title", "content", 1000L, createMember(), createCategory(), images);
    }
}
