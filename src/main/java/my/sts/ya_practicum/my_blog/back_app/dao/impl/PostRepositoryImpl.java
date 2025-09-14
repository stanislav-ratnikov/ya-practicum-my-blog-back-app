package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                    SELECT *, COUNT(*) OVER() AS total_count
                    FROM posts
                    WHERE (:isSearchByTitle = FALSE OR title LIKE :title) AND (:isSearchByTags = FALSE OR tags @> :tags::TEXT[])
                    ORDER BY id DESC
                    LIMIT :limit
                    OFFSET :offset
                    """;

        HashMap<String, Object> params = new HashMap<>();

        params.put("isSearchByTitle", searchCriteria.search() != null);
        params.put("title", searchCriteria.search() == null ? null : "%" + searchCriteria.search() + "%");
        params.put("isSearchByTags", searchCriteria.tags() != null);
        params.put("tags", searchCriteria.tags() == null ? null : searchCriteria.tags().toArray(String[]::new));
        params.put("limit", pageSize);
        params.put("offset", (pageNumber - 1) * pageSize);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> extractPost(rs));
    }

    private Post extractPost(ResultSet rs) throws SQLException {
        Post post = new Post();

        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setText(rs.getString("text"));
        post.setTags(List.of((String[]) rs.getArray("tags").getArray()));
        post.setLikesCount(rs.getLong("likes_count"));

        return post;
    }

    @Override
    public int countTotalPosts(PostSearchCriteria searchCriteria) {
        String sql = """
                    SELECT count(*)
                    FROM posts
                    WHERE (:isSearchByTitle = FALSE OR title LIKE :title) AND (:isSearchByTags = FALSE OR tags @> :tags::TEXT[])
                    """;

        HashMap<String, Object> params = new HashMap<>();

        params.put("isSearchByTitle", searchCriteria.search() != null);
        params.put("title", searchCriteria.search() == null ? null : "%" + searchCriteria.search() + "%");
        params.put("isSearchByTags", searchCriteria.tags() != null);
        params.put("tags", toArray(searchCriteria.tags()));

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public Post findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM posts WHERE id = ?",
                rs -> rs.next() ? extractPost(rs) : null,
                id
        );
    }

    @Override
    public Long save(Post post) {
        Map<String, Object> params = new HashMap<>();

        params.put("title", post.getTitle());
        params.put("text", post.getText());
        params.put("likes_count", post.getLikesCount());
        params.put("tags", toArray(post.getTags()));

        return (Long) new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingColumns("title", "text", "likes_count", "tags")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(params);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET title = ?, text = ?, likes_count = ?, tags = ? WHERE id = ?",
                post.getTitle(),
                post.getText(),
                post.getLikesCount(),
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

    @Override
    public boolean exists(long postId) {
        String sql = "SELECT COUNT(*) FROM posts WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId);

        return count != null && count > 0;
    }
}
