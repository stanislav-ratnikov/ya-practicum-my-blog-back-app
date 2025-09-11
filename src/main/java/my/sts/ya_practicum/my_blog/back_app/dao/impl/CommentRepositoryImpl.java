package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Comment;
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
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return jdbcTemplate.query("select * from comments where post_id = ?", (rs, rowNum) -> extractComment(rs), postId);
    }

    private Comment extractComment(ResultSet rs) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getLong("id"));
        comment.setPostId(rs.getLong("post_id"));
        comment.setText(rs.getString("text"));

        return comment;
    }

    @Override
    public Map<Long, Long> getCommentsCountByPostId(List<Long> postIds) {
        String sql = """
                    SELECT p.id AS post_id, COUNT(c.id) AS comment_count
                    FROM posts p
                    LEFT JOIN comments c ON p.id = c.post_id
                    WHERE p.id IN (:postIds)
                    GROUP BY p.id
                    """;

        Map<String, Object> params = new HashMap<>(){
            {
                put("postIds", postIds);
            }
        };

        return namedParameterJdbcTemplate.query(sql, params, rs -> {
            Map<Long, Long> commentCounts = new HashMap<>();

            while (rs.next()) {
                Long postId = rs.getLong("post_id");
                Long commentCount = rs.getLong("comment_count");

                commentCounts.put(postId, commentCount);
            }

            return commentCounts;
        });
    }

    @Override
    public void deleteByPostId(Long postId) {
        Map<String, Object> params = new HashMap<>(){
            {
                put("postId", postId);
            }
        };

        namedParameterJdbcTemplate.update("DELETE FROM comments WHERE post_id = :postId", params);
    }

    @Override
    public Comment findByPostIdAndCommentId(Long postId, Long commentId) {
        String sql = "SELECT * FROM comments WHERE id = :commentId AND post_id = :postId";

        HashMap<String, Object> params = new HashMap<>() {
            {
                put("postId", postId);
                put("commentId", commentId);
            }
        };

        return namedParameterJdbcTemplate.query(
                sql,
                params, rs -> rs.next() ? extractComment(rs) : null
        );
    }

    @Override
    public Long save(Comment comment) {
        Map<String, Object> params = new HashMap<>() {{
            put("text", comment.getText());
            put("post_id", comment.getPostId());
        }};

        return (Long) new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(params);
    }
}
