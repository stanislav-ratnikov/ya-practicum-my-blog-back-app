package my.sts.ya_practicum.my_blog.back_app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tag {
    private long id;
    private String name;
    private Long postId;
}
