package my.sts.ya_practicum.my_blog.back_app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import my.sts.ya_practicum.my_blog.back_app.web.validation.group.Update;

import java.util.List;

@Getter
@Setter
public class PostDto {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    @Size(max = 256)
    private String title;

    @NotBlank
    @Size(max = 256)
    private String text;

    @NotEmpty
    private List<String> tags;

    private Long likesCount;
    private Long commentsCount;
}
