package my.sts.ya_practicum.my_blog.back_app.util.mapper;

import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PostDtoMapper {

    public static List<PostDto> map(List<Post> posts, Map<Long, List<String>> tagsByPostIds) {
        if (posts == null) {
            return Collections.emptyList();
        }

        return posts.stream()
                .map(p -> map(p, tagsByPostIds.get(p.getId())))
                .toList();
    }

    public static PostDto map(Post post, List<String> tags) {
        if (post == null) {
            return null;
        }

        PostDto dto = new PostDto();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setTags(tags == null ? Collections.emptyList() : tags);

        //todo:
        dto.setLikeCount(0L);
        dto.setCommentCount(0L);

        return dto;
    }
}
