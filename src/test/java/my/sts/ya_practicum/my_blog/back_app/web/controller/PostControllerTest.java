package my.sts.ya_practicum.my_blog.back_app.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.sts.ya_practicum.my_blog.back_app.service.PostService;
import my.sts.ya_practicum.my_blog.back_app.web.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    public void shouldReturnValidResponse_whenGetPosts() throws Exception {
        PostDto postDto = new PostDto();

        postDto.setId(42L);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setLikesCount(10L);
        postDto.setCommentsCount(10L);
        postDto.setTags(List.of("tag1", "tag2"));

        FindPostsResponseDto findPostsResponseDto = new FindPostsResponseDto();

        findPostsResponseDto.setPosts(List.of(postDto));

        when(postService.findPosts(any(), any(), any())).thenReturn(findPostsResponseDto);

        mockMvc.perform(
                get("/api/posts").contentType(MediaType.APPLICATION_JSON)
                        .param("search", "")
                        .param("pageNumber", "1")
                        .param("pageSize", "5")
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.posts").isArray(),
                jsonPath("$.posts.length()").value(1),
                jsonPath("$.posts[0].id").value(42),
                jsonPath("$.posts[0].title").value("title"),
                jsonPath("$.posts[0].text").value("text"),
                jsonPath("$.posts[0].likesCount").value(10),
                jsonPath("$.posts[0].commentsCount").value(10),
                jsonPath("$.posts[0].tags").isArray(),
                jsonPath("$.posts[0].tags[0]").value("tag1"),
                jsonPath("$.posts[0].tags[1]").value("tag2")
        );
    }

    @Test
    public void shouldReturnValidResponse_whenGetPostById() throws Exception {
        long postId = 42L;

        PostDto postDto = new PostDto();

        postDto.setId(postId);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setLikesCount(10L);
        postDto.setCommentsCount(10L);
        postDto.setTags(List.of("tag1", "tag2"));

        when(postService.findById(postId)).thenReturn(postDto);

        mockMvc.perform(get("/api/posts/{id}", postId).contentType(MediaType.APPLICATION_JSON)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isMap(),
                jsonPath("$.id").value(42),
                jsonPath("$.title").value("title"),
                jsonPath("$.text").value("text"),
                jsonPath("$.likesCount").value(10),
                jsonPath("$.commentsCount").value(10),
                jsonPath("$.tags").isArray(),
                jsonPath("$.tags[0]").value("tag1"),
                jsonPath("$.tags[1]").value("tag2")
        );
    }

    @Test
    public void shouldReturnValidResponse_whenCreatePost() throws Exception {
        PostDto postDto = new PostDto();

        postDto.setId(42L);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setLikesCount(10L);
        postDto.setCommentsCount(10L);
        postDto.setTags(List.of("tag1", "tag2"));

        when(postService.createPost(any())).thenReturn(postDto);

        mockMvc.perform(
                post("/api/posts")
                        .content(objectMapper.writeValueAsString(postDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isMap(),
                jsonPath("$.id").value(42),
                jsonPath("$.title").value("title"),
                jsonPath("$.text").value("text"),
                jsonPath("$.likesCount").value(10),
                jsonPath("$.commentsCount").value(10),
                jsonPath("$.tags").isArray(),
                jsonPath("$.tags[0]").value("tag1"),
                jsonPath("$.tags[1]").value("tag2")
        );
    }

    @Test
    public void shouldReturnValidResponse_whenUpdatePost() throws Exception {
        PostDto postDto = new PostDto();

        postDto.setId(42L);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setLikesCount(10L);
        postDto.setCommentsCount(10L);
        postDto.setTags(List.of("tag1", "tag2"));

        when(postService.exists(any())).thenReturn(true);
        when(postService.updatePost(any(), any())).thenReturn(postDto);

        mockMvc.perform(
                put("/api/posts/{id}", 42)
                        .content(objectMapper.writeValueAsString(postDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isMap(),
                jsonPath("$.id").value(42),
                jsonPath("$.title").value("title"),
                jsonPath("$.text").value("text"),
                jsonPath("$.likesCount").value(10),
                jsonPath("$.commentsCount").value(10),
                jsonPath("$.tags").isArray(),
                jsonPath("$.tags[0]").value("tag1"),
                jsonPath("$.tags[1]").value("tag2")
        );
    }

    @Test
    public void shouldReturnValidResponse_whenDeletePost() throws Exception {
        when(postService.exists(any())).thenReturn(true);

        mockMvc.perform(delete("/api/posts/{id}", 42).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnValidResponse_whenIncrementLikesCount() throws Exception {
        when(postService.exists(any())).thenReturn(true);
        when(postService.incrementLikesCount(any())).thenReturn(100500L);

        mockMvc.perform(post("/api/posts/{id}/likes", 42).contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().string(String.valueOf(100500))
                );
    }
}
