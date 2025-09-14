package my.sts.ya_practicum.my_blog.back_app.util.mapper;

import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PostDtoMapper {

    public static List<PostDto> map(List<Post> posts, Map<Long, Long> commentsCountMap) {
        if (posts == null) {
            return Collections.emptyList();
        }

        return posts.stream()
                .map(p -> map(p, commentsCountMap.get(p.getId())))
                .toList();
    }

    public static PostDto map(Post post, Long commentsCount) {
        if (post == null) {
            return null;
        }

        PostDto dto = new PostDto();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setTags(post.getTags());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(commentsCount);

        return dto;
    }

    public static Post map(PostDto postDto) {
        if (postDto == null) {
            return null;
        }

        Post post = new Post();

        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setLikesCount(postDto.getLikesCount());
        post.setTags(postDto.getTags());

        return post;
    }
}
