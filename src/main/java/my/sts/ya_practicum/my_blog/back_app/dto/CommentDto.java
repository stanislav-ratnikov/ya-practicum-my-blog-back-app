package my.sts.ya_practicum.my_blog.back_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private Long postId;
    private String text;
}
