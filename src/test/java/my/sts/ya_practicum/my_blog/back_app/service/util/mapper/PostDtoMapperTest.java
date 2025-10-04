package my.sts.ya_practicum.my_blog.back_app.service.util.mapper;

import my.sts.ya_practicum.my_blog.back_app.persistence.model.Post;
import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostDtoMapperTest {

    private static final Post testPost = Post.builder()
            .id(42L)
            .title("title")
            .text("text")
            .likesCount(123L)
            .tags(List.of("tag1", "tag2"))
            .build();

    private static final Long testPostCommentsCount = 123L;

    @Test
    public void shouldReturnNull_whenPostIsNull() {
        assertNull(PostDtoMapper.map(null, (Long) null));
        assertNull(PostDtoMapper.map(null, 42L));
    }

    @ParameterizedTest
    @MethodSource("validPosts")
    public void shouldReturnValidResult_whenPostIsValid(Post post, Long commentsCount) {
        PostDto result = PostDtoMapper.map(post, commentsCount);

        validateResult(post, commentsCount, result);
    }

    public static Stream<Arguments> validPosts() {
        return Stream.of(
                Arguments.of(testPost, testPostCommentsCount)
        );
    }

    private void validateResult(Post post, Long commentsCount, PostDto result) {
        assertNotNull(result);

        assertEquals(post.getId(), result.getId());
        assertEquals(post.getTitle(), result.getTitle());
        assertEquals(post.getText(), result.getText());
        assertEquals(post.getLikesCount(), result.getLikesCount());
        assertIterableEquals(post.getTags(), result.getTags());

        assertEquals(commentsCount, result.getCommentsCount());
    }

    @Test
    public void shouldReturnEmptyList_whenPostListIsNull() {
        assertNotNull(PostDtoMapper.map(null, (Map<Long, Long>) null));
        assertTrue(PostDtoMapper.map(null, (Map<Long, Long>) null).isEmpty());

        assertNotNull(PostDtoMapper.map(null, new HashMap<>()));
        assertTrue(PostDtoMapper.map(null, new HashMap<>()).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("validPostLists")
    public void shouldReturnValidResults_whenPostsAreValid(List<Post> posts, Map<Long, Long> commentsCounts) {
        List<PostDto> resultList = PostDtoMapper.map(posts, commentsCounts);

        assertNotNull(resultList);
        assertEquals(posts.size(), resultList.size());

        Map<Long, Post> expectedPosts = posts.stream()
                .collect(Collectors.toMap(Post::getId, Function.identity()));

        resultList.forEach(result -> validateResult(
                expectedPosts.get(result.getId()),
                commentsCounts.get(result.getId()),
                result
        ));
    }

    public static Stream<Arguments> validPostLists() {
        return Stream.of(
                Arguments.of(List.of(testPost), Map.of(testPost.getId(), testPostCommentsCount))
        );
    }

    @Test
    public void shouldReturnNull_whenPostDtoIsNull() {
        assertNull(PostDtoMapper.map(null));
    }

    @Test
    public void shouldReturnValidResult_whenPostDtoIsValid() {
        PostDto postDto = new PostDto();

        postDto.setId(42L);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setTags(List.of("tag1", "tag2"));
        postDto.setLikesCount(123L);

        Post result = PostDtoMapper.map(postDto);

        assertNotNull(result);

        assertEquals(postDto.getId(), result.getId());
        assertEquals(postDto.getTitle(), result.getTitle());
        assertEquals(postDto.getText(), result.getText());
        assertEquals(postDto.getLikesCount(), result.getLikesCount());
        assertIterableEquals(postDto.getTags(), result.getTags());
    }
}
