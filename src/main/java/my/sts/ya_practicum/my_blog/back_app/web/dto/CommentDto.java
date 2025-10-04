package my.sts.ya_practicum.my_blog.back_app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import my.sts.ya_practicum.my_blog.back_app.web.validation.group.Update;

@Getter
@Setter
public class CommentDto {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private Long postId;

    @NotBlank
    @Size(max = 256)
    private String text;
}
