package my.sts.ya_practicum.my_blog.back_app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FindPostsResponseDto {
    private List<PostDto> posts;
}
