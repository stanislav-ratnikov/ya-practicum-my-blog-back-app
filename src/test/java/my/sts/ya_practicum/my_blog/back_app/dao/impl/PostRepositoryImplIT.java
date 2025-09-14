package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.config.DataSourceConfig;
import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {DataSourceConfig.class, PostRepositoryImpl.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class PostRepositoryImplIT {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    void findPosts_shouldReturnValidResults_whenPostsExist() {
        List<Post> posts = postRepository.find(new PostSearchCriteria("", List.of()), 1, 5);

        assertNotNull(posts);
        assertEquals(1, posts.size());

        validate(posts.getFirst());
    }

    @Test
    void findById_shouldReturnValidResult_whenPostExists() {
        validate(postRepository.findById(1));
    }

    private void validate(Post post) {
        assertNotNull(post);
        assertEquals(1, post.getId());
        assertEquals("тест_пост1", post.getTitle());
        assertEquals("тест_пост1_текст", post.getText());
        assertEquals(0, post.getLikesCount());
        assertNotNull(post.getTags());
        assertEquals(1, post.getTags().size());
        assertEquals("тест_пост1_тег1", post.getTags().get(0));
    }

    @Test
    void countTotalPosts_shouldReturnCount_whenSearchCriteriaMatchesExistingPosts() {
        int totalPosts = postRepository.countTotalPosts(new PostSearchCriteria("", List.of()));

        assertEquals(1, totalPosts);
    }

    @Test
    void countTotalPosts_shouldReturnZero_whenSearchCriteriaDoesntMatchAnyPost() {
        int totalPosts = postRepository.countTotalPosts(new PostSearchCriteria("nonexistent-title", List.of("nonexistent-tag")));

        assertEquals(0, totalPosts);
    }
}
