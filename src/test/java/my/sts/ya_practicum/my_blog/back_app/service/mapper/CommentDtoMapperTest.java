package my.sts.ya_practicum.my_blog.back_app.service.mapper;

import my.sts.ya_practicum.my_blog.back_app.persistence.model.Comment;
import my.sts.ya_practicum.my_blog.back_app.web.dto.CommentDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentDtoMapperTest {

    private static final Comment testComment = Comment.builder()
            .id(123L)
            .postId(42L)
            .text("comment_text")
            .build();

    @Test
    public void shouldReturnNull_whenCommentIsNull() {
        assertNull(CommentDtoMapper.map((Comment) null));
    }

    @Test
    public void shouldReturnEmptyList_whenCommentsAreNull() {
        List<CommentDto> result = CommentDtoMapper.map((List<Comment>) null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnValidResult_whenCommentIsValid() {
        validateResult(testComment, CommentDtoMapper.map(testComment));
    }

    @Test
    public void shouldReturnValidResultList_whenCommentsAreValid() {
        List<CommentDto> result = CommentDtoMapper.map(List.of(testComment));

        assertNotNull(result);
        assertEquals(1, result.size());

        validateResult(testComment, result.getFirst());
    }

    private void validateResult(Comment comment, CommentDto result) {
        assertNotNull(result);

        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getPostId(), result.getPostId());
        assertEquals(comment.getText(), result.getText());
    }
}
