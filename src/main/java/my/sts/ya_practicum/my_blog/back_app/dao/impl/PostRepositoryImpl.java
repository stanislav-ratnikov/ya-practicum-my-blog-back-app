package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> {
        Post post = new Post();

        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setText(rs.getString("text"));
        post.setTags(List.of((String[]) rs.getArray("tags").getArray()));
        post.setLikesCount(rs.getLong("likes_count"));

        return post;
    };

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Post> find(PostSearchCriteria searchCriteria, Integer pageNumber, Integer pageSize) {
        String sql = """
                    SELECT *
                    FROM posts
                    WHERE (:searchTitle = FALSE OR title LIKE :search) AND (:searchTags = FALSE OR tags @> :tags::TEXT[])
                    ORDER BY id
                    LIMIT :limit
                    OFFSET :offset
                    """;

        HashMap<String, Object> params = new HashMap<>();

        params.put("searchTitle", searchCriteria.searchSubString() != null);
        params.put("search", searchCriteria.searchSubString() == null ? null : "%" + searchCriteria.searchSubString() + "%");
        params.put("searchTags", searchCriteria.tags() != null);
        params.put("tags", searchCriteria.tags() == null ? null : searchCriteria.tags().toArray(String[]::new));
        params.put("limit", pageSize);
        params.put("offset", (pageNumber - 1) * pageSize);

        return namedParameterJdbcTemplate.query(sql, params, postRowMapper);
    }

    @Override
    public int count(PostSearchCriteria searchCriteria) {
        String sql = """
                    SELECT count(*)
                    FROM posts
                    WHERE (:searchTitle = FALSE OR title LIKE :search) AND (:searchTags = FALSE OR tags @> :tags::TEXT[])
                    """;

        HashMap<String, Object> params = new HashMap<>();

        params.put("searchTitle", searchCriteria.searchSubString() != null);
        params.put("search", searchCriteria.searchSubString() == null ? null : "%" + searchCriteria.searchSubString() + "%");
        params.put("searchTags", searchCriteria.tags() != null);
        params.put("tags", toArray(searchCriteria.tags()));

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public Post findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM posts WHERE id = ?", postRowMapper, id);
    }

    @Override
    public Long save(Post post) {
        Map<String, Object> params = new HashMap<>();

        params.put("title", post.getTitle());
        params.put("text", post.getText());
        params.put("tags", toArray(post.getTags()));

        return (Long) new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingColumns("title", "text", "tags")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(params);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET title = ?, text = ?, tags = ? WHERE id = ?",
                post.getText(),
                post.getTitle(),
                toArray(post.getTags()),
                post.getId()
        );
    }
    
    private String[] toArray(List<String> list) {
        return list == null ? null : list.toArray(new String[0]);
    }

    @Override
    public void deletePost(long postId) {
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", postId);
    }

    @Override
    public Long incrementLikes(long postId) {
        String sql = "UPDATE posts SET likes_count = likes_count + 1 WHERE id = ? RETURNING likes_count";

        return jdbcTemplate.queryForObject(sql, Long.class, postId);
    }
}
