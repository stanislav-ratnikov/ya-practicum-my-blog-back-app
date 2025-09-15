package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.config.DataSourceConfig;
import my.sts.ya_practicum.my_blog.back_app.dao.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({
        @ContextConfiguration(classes = DataSourceConfig.class),
        @ContextConfiguration(classes = CommentRepositoryImpl.class),
})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentRepositoryImplIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("TRUNCATE TABLE posts RESTART IDENTITY CASCADE");
        jdbcTemplate.update("INSERT INTO posts(title, text, tags) VALUES ('тест_пост1', 'тест_пост1_текст', ARRAY['тест_пост1_тег1'])");
        jdbcTemplate.update("""
                INSERT INTO comments (post_id, text)
                VALUES ((SELECT id FROM posts WHERE title = 'тест_пост1'), 'тест_пост1_комментарий1')
                """);
    }

    @Test
    void findByPostId_shouldReturnPostComments_whenPostExists_andCommentsExist() {
        List<Comment> comments = commentRepository.findByPostId(1L);

        assertNotNull(comments);
        assertEquals(1, comments.size());

        Comment comment = comments.getFirst();

        assertNotNull(comment);
        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("тест_пост1_комментарий1", comment.getText());
    }

    private void validate(Comment comment) {
        assertNotNull(comment);
        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("тест_пост1_комментарий1", comment.getText());
    }

    @Test
    void findByPostId_shouldReturnEmptyList_whenPostNotExists() {
        List<Comment> comments = commentRepository.findByPostId(42L);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void findByPostId_shouldReturnEmptyList_whenPostExists_andCommentsNotExist() {
        jdbcTemplate.update("DELETE FROM comments WHERE post_id = ?", 1L);

        List<Comment> comments = commentRepository.findByPostId(1L);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void findByPostIdAndCommentId_shouldReturnComment_whenPostExists_andCommentExists() {
        validate(commentRepository.findByPostIdAndCommentId(1L, 1L));
    }

    @Test
    void getCommentsCountByPostId_shouldReturnCommentCounts_whenPostsExist() {
        Map<Long, Long> commentsCounts = commentRepository.getCommentsCountByPostId(List.of(1L));

        assertNotNull(commentsCounts);
        assertEquals(1, commentsCounts.size());
        assertNotNull(commentsCounts.get(1L));
        assertEquals(1L, commentsCounts.get(1L));
    }

    @Test
    void deleteByPostId_shouldDeleteAllComments_whenPostsExist() {
        commentRepository.deleteByPostId(1L);

        List<Comment> comments = commentRepository.findByPostId(1L);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void save_shouldCreateNewComment_whenPostExists() {
        Comment comment = new Comment();

        comment.setPostId(1L);
        comment.setText("sample_text");

        Long commentId = commentRepository.save(comment);

        assertNotNull(commentId);
        assertEquals(2L, commentId);

        Comment savedComment = commentRepository.findByPostIdAndCommentId(1L, 2L);

        validate(savedComment, comment, commentId);
    }

    @Test
    void update_shouldUpdateComment_whenCommentExists() {
        Comment comment = new Comment();

        comment.setId(1L);
        comment.setPostId(1L);
        comment.setText("sample_text");

        commentRepository.update(comment);

        Comment savedComment = commentRepository.findByPostIdAndCommentId(1L, 1L);

        validate(savedComment, comment, comment.getId());
    }

    private void validate(Comment comment, Comment expected, Long expectedId) {
        assertNotNull(comment);
        assertEquals(expectedId, comment.getId());
        assertEquals(expected.getPostId(), comment.getPostId());
        assertEquals(expected.getText(), comment.getText());
    }

    @Test
    void delete_shouldDeleteComment_whenCommentExists() {
        assertNotNull(commentRepository.findByPostIdAndCommentId(1L, 1L));

        commentRepository.delete(1L, 1L);

        assertNull(commentRepository.findByPostIdAndCommentId(1L, 1L));
    }
}
