package my.sts.ya_practicum.my_blog.back_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.sts.ya_practicum.my_blog.back_app.config.WebConfig;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@TestPropertySource("classpath:application-test.properties")
class PostControllerIT {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.update("TRUNCATE TABLE posts RESTART IDENTITY CASCADE");
        jdbcTemplate.update("INSERT INTO posts(title, text, tags) VALUES ('тест_пост1', 'тест_пост1_текст', ARRAY['тест_пост1_тег1'])");
        jdbcTemplate.update("""
                INSERT INTO comments (post_id, text)
                VALUES ((SELECT id FROM posts WHERE title = 'тест_пост1'), 'тест_пост1_комментарий1')
                """);
    }

    @Test
    public void getPosts_shouldReturnValidResults_whenPostWithCommentExists() throws Exception {
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
                jsonPath("$.posts[0].id").value(1),
                jsonPath("$.posts[0].title").value("тест_пост1"),
                jsonPath("$.posts[0].text").value("тест_пост1_текст"),
                jsonPath("$.posts[0].likesCount").value(0),
                jsonPath("$.posts[0].commentsCount").value(1),
                jsonPath("$.posts[0].tags").isArray(),
                jsonPath("$.posts[0].tags.length()").value(1),
                jsonPath("$.posts[0].tags[0]").value("тест_пост1_тег1")
        );
    }

    @Test
    public void getPostById_shouldReturnValidResponse_whenPostWithCommentExists() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isMap(),
                jsonPath("$.id").value(1),
                jsonPath("$.title").value("тест_пост1"),
                jsonPath("$.text").value("тест_пост1_текст"),
                jsonPath("$.likesCount").value(0),
                jsonPath("$.commentsCount").value(1),
                jsonPath("$.tags").isArray(),
                jsonPath("$.tags.length()").value(1),
                jsonPath("$.tags[0]").value("тест_пост1_тег1")
        );
    }

    @Test
    public void createPost_shouldReturnCreatedPost_whenExecutedSuccessfully() throws Exception {
        PostDto postDto = new PostDto();

        postDto.setTitle("тест_пост2");
        postDto.setText("тест_пост2_текст");
        postDto.setLikesCount(2L);
        postDto.setTags(List.of("тест_пост2_тег1", "тест_пост2_тег2"));

        mockMvc.perform(
                        post("/api/posts")
                                .content(objectMapper.writeValueAsString(postDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$").isMap(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.title").value("тест_пост2"),
                        jsonPath("$.text").value("тест_пост2_текст"),
                        jsonPath("$.likesCount").value(2),
                        jsonPath("$.commentsCount").value(0),
                        jsonPath("$.tags").isArray(),
                        jsonPath("$.tags.length()").value(2),
                        jsonPath("$.tags[0]").value("тест_пост2_тег1"),
                        jsonPath("$.tags[1]").value("тест_пост2_тег2")
                );
    }

    @Test
    public void updatePost_shouldReturnValidResponse_whenPostDtoIsValid() throws Exception {
        PostDto postDto = new PostDto();

        postDto.setId(1L);
        postDto.setTitle("тест_пост1 <updated>");
        postDto.setText("тест_пост1_текст <updated>");
        postDto.setLikesCount(42L);
        postDto.setTags(List.of("тест_пост1_тег1", "тест_пост1_тег2"));

        mockMvc.perform(
                put("/api/posts/{id}", 1)
                        .content(objectMapper.writeValueAsString(postDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isMap(),
                jsonPath("$.id").value(postDto.getId()),
                jsonPath("$.title").value(postDto.getTitle()),
                jsonPath("$.text").value(postDto.getText()),
                jsonPath("$.likesCount").value(postDto.getLikesCount()),
                jsonPath("$.commentsCount").value(1),
                jsonPath("$.tags").isArray(),
                jsonPath("$.tags.length()").value(postDto.getTags().size()),
                jsonPath("$.tags[0]").value(postDto.getTags().get(0)),
                jsonPath("$.tags[1]").value(postDto.getTags().get(1))
        );
    }

    @Test
    public void updatePost_shouldReturnStatusNotFound_whenPostNotExists() throws Exception {
        PostDto postDto = new PostDto();

        postDto.setId(42L);
        postDto.setTitle("тест_пост1 <updated>");
        postDto.setText("тест_пост1_текст <updated>");
        postDto.setLikesCount(42L);
        postDto.setTags(List.of("тест_пост1_тег1", "тест_пост1_тег2"));

        mockMvc.perform(
                        put("/api/posts/{id}", 42)
                                .content(objectMapper.writeValueAsString(postDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePost_shouldReturnStatusBadRequest_whenEmptyRequestBody() throws Exception {
        mockMvc.perform(put("/api/posts/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletePost_shouldReturnStatusNoContent_whenSuccessfullyDeleted() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePost_shouldReturnStatusNotFound_whenPostNotExists() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 42).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void incrementLikes_shouldReturnNewLikesCount_whenIncrementedSuccessfully() throws Exception {
        mockMvc.perform(post("/api/posts/{id}/likes", 1))
                .andExpectAll(
                        status().isOk(),
                        content().string(String.valueOf(1))
                );

        mockMvc.perform(post("/api/posts/{id}/likes", 1))
                .andExpectAll(
                        status().isOk(),
                        content().string(String.valueOf(2))
                );
    }

    @Test
    public void incrementLikes_shouldReturnStatusNotFound_whenPostNotExist() throws Exception {
        mockMvc.perform(post("/api/posts/{id}/likes", 42)).andExpect(status().isNotFound());
    }
}
