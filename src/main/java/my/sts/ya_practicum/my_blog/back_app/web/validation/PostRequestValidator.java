package my.sts.ya_practicum.my_blog.back_app.web.validation;

import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.web.exception.RequestValidationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PostRequestValidator {
    public void validate(Long postId, PostDto postDto) {
        if (!Objects.equals(postId, postDto.getId())) {
            throw new RequestValidationException();
        }
    }
}
