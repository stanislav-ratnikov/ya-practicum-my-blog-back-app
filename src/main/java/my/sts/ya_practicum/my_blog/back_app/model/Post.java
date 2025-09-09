package my.sts.ya_practicum.my_blog.back_app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
    private Long id;
    private String title;
    private String text;
    private Long likeCount;
}
