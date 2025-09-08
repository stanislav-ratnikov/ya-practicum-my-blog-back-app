package my.sts.ya_practicum.my_blog.back_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String text;
}
