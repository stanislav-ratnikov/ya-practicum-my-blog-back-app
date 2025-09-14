package my.sts.ya_practicum.my_blog.back_app.controller;

import my.sts.ya_practicum.my_blog.back_app.config.WebConfig;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@TestPropertySource("classpath:application-test.properties")
class PostControllerIT {

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
}
