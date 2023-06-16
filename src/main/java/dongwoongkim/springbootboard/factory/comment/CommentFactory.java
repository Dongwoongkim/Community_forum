package dongwoongkim.springbootboard.factory.comment;

import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.post.Post;

import static dongwoongkim.springbootboard.factory.member.MemberFactory.createMember;
import static dongwoongkim.springbootboard.factory.post.PostFactory.createPost;

public class CommentFactory {

    public static Comment createComment(Comment parent) {
        return new Comment("content", createMember(), createPost(), parent);
    }

    public static Comment createComment(Member member, Post post, Comment parent) {
        return new Comment("content", member, post, parent);
    }
}
