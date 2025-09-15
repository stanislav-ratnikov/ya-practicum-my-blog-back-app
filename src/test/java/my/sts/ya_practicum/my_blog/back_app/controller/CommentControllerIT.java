package my.sts.ya_practicum.my_blog.back_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.sts.ya_practicum.my_blog.back_app.config.WebConfig;
import my.sts.ya_practicum.my_blog.back_app.dto.CommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class CommentControllerIT {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(ApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.update("TRUNCATE TABLE posts RESTART IDENTITY CASCADE");
        jdbcTemplate.update("INSERT INTO posts(title, text, tags) VALUES ('тест_пост1', 'тест_пост1_текст', ARRAY['тест_пост1_тег1'])");
        jdbcTemplate.update("""
                INSERT INTO comments (post_id, text)
                VALUES ((SELECT id FROM posts WHERE title = 'тест_пост1'), 'тест_пост1_комментарий1')
                """);
    }

    @Test
    void getComments_shouldReturnCommentsList_whenPostExists_andCommentsExist() throws Exception {
        mockMvc.perform(get("/api/posts/{id}/comments", 1)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(1),
                jsonPath("$[0].id").value(1),
                jsonPath("$[0].postId").value(1),
                jsonPath("$[0].text").value("тест_пост1_комментарий1")
        );
    }

    @Test
    void getComments_shouldReturnStatusNotFound_whenPostNotExists() throws Exception {
        mockMvc.perform(get("/api/posts/{id}/comments", 42)).andExpect(status().isNotFound());
    }

    @Test
    void getComment_shouldReturnComment_whenPostExists_andCommentExist() throws Exception {
        mockMvc.perform(get("/api/posts/{id}/comments/{commentId}", 1, 1)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").isMap(),
                jsonPath("$.id").value(1),
                jsonPath("$.postId").value(1),
                jsonPath("$.text").value("тест_пост1_комментарий1")
        );
    }

    @Test
    void getComment_shouldReturnStatusNotFound_whenPostNotExists() throws Exception {
        mockMvc.perform(get("/api/posts/{id}/comments/{commentId}", 42, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getComment_shouldReturnStatusNotFound_whenPostExists_andCommentNotExist() throws Exception {
        mockMvc.perform(get("/api/posts/{id}/comments/{commentId}", 1, 42))
                .andExpect(status().isNotFound());
    }

    @Test
    void createComment_shouldReturnNewComment_whenPostExists_andSavedSuccessfully() throws Exception {
        CommentDto commentDto = new CommentDto();

        commentDto.setPostId(1L);
        commentDto.setText("тест_пост1_комментарий2");

        mockMvc.perform(
                post("/api/posts/{id}/comments", 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$").isMap(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.postId").value(1),
                        jsonPath("$.text").value("тест_пост1_комментарий2")
                );
    }

    @Test
    void createComment_shouldReturnStatusNotFound_whenPostNotExist() throws Exception {
        final long postId = 42L;

        CommentDto commentDto = new CommentDto();

        commentDto.setPostId(postId);
        commentDto.setText("тест_пост1_комментарий2");

        mockMvc.perform(
                post("/api/posts/{id}/comments", postId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void updateComment_shouldReturnUpdatedComment_whenPostExists_andCommentExist() throws Exception {
        final long postId = 1L;
        final long commentId = 1L;

        CommentDto commentDto = new CommentDto();

        commentDto.setId(commentId);
        commentDto.setPostId(postId);
        commentDto.setText("тест_пост1_комментарий_updated");

        mockMvc.perform(
                        put("/api/posts/{id}/comments/{commentId}", postId, commentId)
                                .content(objectMapper.writeValueAsString(commentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$").isMap(),
                        jsonPath("$.id").value(commentId),
                        jsonPath("$.postId").value(postId),
                        jsonPath("$.text").value(commentDto.getText())
                );
    }

    @Test
    void updateComment_shouldReturnStatusNotFound_whenPostNotExists() throws Exception {
        final long postId = 2L;
        final long commentId = 1L;

        CommentDto commentDto = new CommentDto();

        commentDto.setId(commentId);
        commentDto.setPostId(postId);
        commentDto.setText("тест_пост1_комментарий_updated");

        mockMvc.perform(
                        put("/api/posts/{id}/comments/{commentId}", postId, commentId)
                                .content(objectMapper.writeValueAsString(commentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateComment_shouldReturnStatusNotFound_whenPostExists_andCommentNotExists() throws Exception {
        final long postId = 1L;
        final long commentId = 2L;

        CommentDto commentDto = new CommentDto();

        commentDto.setId(commentId);
        commentDto.setPostId(postId);
        commentDto.setText("тест_пост1_комментарий_updated");

        mockMvc.perform(
                        put("/api/posts/{id}/comments/{commentId}", postId, commentId)
                                .content(objectMapper.writeValueAsString(commentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment_shouldReturnStatusNoContent_whenPostExists_andCommentDeletedSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}/comments/{commentId}", 1, 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteComment_shouldReturnStatusNotFound_whenPostNotExists() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}/comments/{commentId}", 2, 1))
                .andExpect(status().isNotFound());
    }
}
