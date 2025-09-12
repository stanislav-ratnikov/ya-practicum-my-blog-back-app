package my.sts.ya_practicum.my_blog.back_app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Comment {
    private Long id;
    private Long postId;
    private String text;
}
