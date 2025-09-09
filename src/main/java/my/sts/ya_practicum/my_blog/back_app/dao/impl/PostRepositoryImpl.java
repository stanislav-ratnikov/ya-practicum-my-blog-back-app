package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Post> find(PostSearchCriteria searchCriteria, Integer pageNumber, Integer pageSize) {
        String sql = """
                    SELECT p.*
                    FROM posts p
                    LEFT JOIN tags t ON t.post_id = p.id
                    WHERE (:search is null OR title like :search) AND (:tags is null or t.tag IN (:tags))
                    ORDER BY id
                    LIMIT :limit
                    OFFSET :offset
                    """;

        HashMap<String, Object> params = new HashMap<>();

        params.put("search", searchCriteria.searchSubString() == null ? null : "%" + searchCriteria.searchSubString() + "%");
        params.put("tags", searchCriteria.tags());
        params.put("limit", pageSize);
        params.put("offset", (pageNumber - 1) * pageSize);

        return namedParameterJdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setText(rs.getString("text"));
                    post.setLikeCount(rs.getLong("like_count"));

                    return post;
                }
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
                    post.setLikeCount(rs.getLong("like_count"));

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
