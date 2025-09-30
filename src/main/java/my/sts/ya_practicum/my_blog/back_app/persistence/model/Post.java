package my.sts.ya_practicum.my_blog.back_app.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Post {
    private Long id;
    private String title;
    private String text;
    private List<String> tags;
    private Long likesCount;
}
