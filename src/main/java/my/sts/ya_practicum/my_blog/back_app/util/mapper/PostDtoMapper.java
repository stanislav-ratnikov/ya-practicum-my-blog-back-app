package my.sts.ya_practicum.my_blog.back_app.util.mapper;

import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PostDtoMapper {

    public static List<PostDto> map(List<Post> posts, Map<Long, Long> commentCounts) {
        if (posts == null) {
            return Collections.emptyList();
        }

        return posts.stream()
                .map(p -> map(p, commentCounts.get(p.getId())))
                .toList();
    }

    public static PostDto map(Post post, Long commentCount) {
        if (post == null) {
            return null;
        }

        PostDto dto = new PostDto();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setTags(post.getTags());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(commentCount);

        return dto;
    }
}
