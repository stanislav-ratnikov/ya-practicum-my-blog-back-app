package my.sts.ya_practicum.my_blog.back_app.service.util.mapper;

import my.sts.ya_practicum.my_blog.back_app.web.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.persistence.model.Comment;

import java.util.Collections;
import java.util.List;

public class CommentDtoMapper {

    public static List<CommentDto> map(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(CommentDtoMapper::map)
                .toList();
    }

    public static CommentDto map(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto dto = new CommentDto();

        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setText(comment.getText());

        return dto;
    }
}
