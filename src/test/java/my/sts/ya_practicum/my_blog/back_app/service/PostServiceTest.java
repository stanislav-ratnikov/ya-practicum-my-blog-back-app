package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.persistence.model.Post;
import my.sts.ya_practicum.my_blog.back_app.persistence.repository.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.persistence.repository.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.service.mapper.PostDtoMapper;
import my.sts.ya_practicum.my_blog.back_app.web.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static final Post testPost = Post.builder()
            .id(1L)
            .title("title")
            .text("text")
            .likesCount(1L)
            .tags(List.of("tag1", "tag2"))
            .build();

    @Mock
    private PostValidationService postValidationService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void findPosts_shouldReturnValidResponseDto_whenPostsFound() {
        when(postRepository.find(any(), any(), any())).thenReturn(List.of(testPost));
        when(postRepository.countTotalPosts(any())).thenReturn(1);
        when(commentRepository.getCommentsCountByPostId(any())).thenReturn(new HashMap<>(){
            {
                put(testPost.getId(), 1L);
            }
        });

        FindPostsResponseDto responseDto = postService.findPosts("поисковая строка", 1, 5);

        assertEquals(false, responseDto.getHasNext());
        assertEquals(false, responseDto.getHasPrev());
        assertEquals(1, responseDto.getLastPage());

        assertNotNull(responseDto);

        List<PostDto> posts = responseDto.getPosts();

        assertNotNull(posts);
        assertEquals(1, posts.size());

        PostDto postDto = posts.getFirst();

        validate(postDto, testPost);
    }

    @Test
    void findById_shouldReturnPostDto_whenPostFound() {
        when(postRepository.findById(1L)).thenReturn(testPost);
        when(commentRepository.getCommentsCountByPostId(List.of(1L))).thenReturn(new HashMap<>(){
            {
                put(1L, 1L);
            }
        });

        PostDto postDto = postService.findById(1L);

        validate(postDto, testPost);
    }

    @Test
    void createPost_shouldReturnPostDto_whenSavedAndLoadedSuccessfully() {
        when(postRepository.save(any())).thenReturn(1L);
        when(postRepository.findById(1L)).thenReturn(testPost);
        when(commentRepository.getCommentsCountByPostId(any())).thenReturn(new HashMap<>(){
            {
                put(testPost.getId(), 1L);
            }
        });

        PostDto result = postService.createPost(new PostDto());

        validate(result, testPost);
    }

    @Test
    void updatePost_shouldReturnPostDto_whenUpdatedAndLoadedSuccessfully() {
        when(postRepository.findById(testPost.getId())).thenReturn(testPost);
        when(commentRepository.getCommentsCountByPostId(any())).thenReturn(new HashMap<>(){
            {
                put(testPost.getId(), 1L);
            }
        });

        PostDto result = postService.updatePost(testPost.getId(), PostDtoMapper.map(testPost, 42L));

        validate(result, testPost);
    }

    @Test
    void incrementLikesCount_shouldReturnLikesCount_whenIncrementedSuccessfully() {
        when(postRepository.incrementLikes(testPost.getId())).thenReturn(123L);

        Long likesCount = postService.incrementLikesCount(testPost.getId());

        assertNotNull(likesCount);
        assertEquals(123L, likesCount);
    }

    private void validate(PostDto postDto, Post expected) {
        assertNotNull(postDto);
        assertEquals(expected.getId(), postDto.getId());
        assertEquals(expected.getTitle(), postDto.getTitle());
        assertEquals(expected.getText(), postDto.getText());
        assertEquals(expected.getLikesCount(), postDto.getLikesCount());
        assertIterableEquals(expected.getTags(), postDto.getTags());
    }
}
