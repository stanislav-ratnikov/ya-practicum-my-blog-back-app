package my.sts.ya_practicum.my_blog.back_app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private Long id;
    private Long postId;
    private String text;
}
