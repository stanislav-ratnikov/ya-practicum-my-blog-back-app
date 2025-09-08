package my.sts.ya_practicum.my_blog.back_app.util;

import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;

import java.util.List;

public class PostDtoMapper {

    public static List<PostDto> map(List<Post> posts) {
        if (posts == null) {
            return null;
        }

        return posts.stream()
                .map(PostDtoMapper::map)
                .toList();
    }

    public static PostDto map(Post post) {
        if (post == null) {
            return null;
        }

        PostDto dto = new PostDto();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());

        return dto;
    }
}
