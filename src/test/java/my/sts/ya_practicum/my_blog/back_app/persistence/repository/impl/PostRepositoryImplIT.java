package my.sts.ya_practicum.my_blog.back_app.persistence.repository.impl;

import my.sts.ya_practicum.my_blog.back_app.config.DataSourceConfig;
import my.sts.ya_practicum.my_blog.back_app.persistence.repository.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.persistence.model.Post;
import my.sts.ya_practicum.my_blog.back_app.service.search.PostSearchCriteria;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({
        @ContextConfiguration(classes = DataSourceConfig.class),
        @ContextConfiguration(classes = PostRepositoryImpl.class),
})
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
        assertEquals("тест_пост1_тег1", post.getTags().getFirst());
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

    @Test
    void savePost_shouldReturnedNewPostId_whenSavedSuccessfully() {
        Post post = Post.builder()
                .title("новый_пост")
                .text("новый_пост_текст")
                .likesCount(0L)
                .tags(List.of())
                .build();

        Long postId = postRepository.save(post);

        assertNotNull(postId);

        Post savedPost = postRepository.findById(postId);

        validate(savedPost, post, postId);
    }

    private void validate(Post post, Post expected, Long expectedId) {
        assertNotNull(post);
        assertEquals(expectedId, post.getId());
        assertEquals(expected.getTitle(), post.getTitle());
        assertEquals(expected.getText(), post.getText());
        assertEquals(expected.getLikesCount(), post.getLikesCount());
        assertIterableEquals(expected.getTags(), post.getTags());
    }

    @Test
    void updatePost_shouldUpdatePostSuccessfully() {
        Post post = Post.builder()
                .id(1L)
                .title("пост1_updated")
                .text("пост1_текст_updated")
                .likesCount(123L)
                .tags(List.of("тег1_updated"))
                .build();

        postRepository.update(post);

        Post postUpdated = postRepository.findById(post.getId());

        validate(postUpdated, post, post.getId());
    }

    @Test
    void deletePost_ShouldDeletePostSuccessfully() {
        int postId = 1;

        postRepository.deletePost(postId);

        assertNull(postRepository.findById(postId));
    }

    @Test
    void incrementLikes_shouldIncrementLikesCount_andReturnNewLikesCount_whenPostExists() {
        Long likesCount = postRepository.incrementLikes(1L);
        
        assertNotNull(likesCount);
        assertEquals(1L, likesCount);

        Post post = postRepository.findById(1L);

        assertNotNull(post);
        assertEquals(likesCount, post.getLikesCount());
    }

    @Test
    void exists_shouldReturnTrue_whenPostExists() {
        assertTrue(postRepository.exists(1L));
    }
}
