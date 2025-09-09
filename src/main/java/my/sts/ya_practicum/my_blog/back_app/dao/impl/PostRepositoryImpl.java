package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> find(PostSearchCriteria searchCriteria, Integer pageNumber, Integer pageSize) {
        String sql = """
                    SELECT * 
                    FROM posts
                    WHERE title like ?
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?
                    """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setText(rs.getString("text"));

                    return post;
                },
                "%" + searchCriteria.searchSubString() + "%",
                pageSize,
                (pageNumber - 1) * pageSize
        );
    }

    @Override
    public Post findById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM posts WHERE id = ?",
                (rs, rowNum) -> {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setText(rs.getString("text"));

                    return post;
                },
                id
        );
    }

    @Override
    public Long save(Post post) {
        Map<String, Object> params = new HashMap<>();

        params.put("title", post.getTitle());
        params.put("text", post.getText());

        return (Long) new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(params);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET title = ?, text = ? WHERE id = ?",
                post.getText(),
                post.getTitle(),
                post.getId()
        );
    }
}
