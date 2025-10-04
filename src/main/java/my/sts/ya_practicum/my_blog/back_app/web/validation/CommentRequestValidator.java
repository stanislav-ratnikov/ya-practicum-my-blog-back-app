package my.sts.ya_practicum.my_blog.back_app.web.validation;

import my.sts.ya_practicum.my_blog.back_app.web.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.web.exception.RequestValidationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentRequestValidator {

    public void validate(Long postId, CommentDto commentDto) {
        if (!Objects.equals(postId, commentDto.getPostId())) {
            throw new RequestValidationException();
        }
    }

    public void validate(Long postId, Long commentId, CommentDto commentDto) {
        validate(postId, commentDto);

        if (!Objects.equals(commentId, commentDto.getId())) {
            throw new RequestValidationException();
        }
    }
}
