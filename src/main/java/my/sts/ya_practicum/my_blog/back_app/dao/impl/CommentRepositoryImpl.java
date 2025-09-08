package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return jdbcTemplate.query(
                "select * from comments where post_id = ?",
                (rs, rowNum) -> {
                    Comment comment = new Comment();

                    comment.setId(rs.getLong("id"));
                    comment.setPostId(rs.getLong("post_id"));
                    comment.setText(rs.getString("text"));

                    return comment;
                },
                postId
        );
    }
}
