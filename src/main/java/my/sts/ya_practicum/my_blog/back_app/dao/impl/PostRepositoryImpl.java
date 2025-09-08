package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "select * from posts",
                (rs, rowNum) -> {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setText(rs.getString("text"));

                    return post;
                }
        );
    }

    @Override
    public Post findById(long id) {
        return jdbcTemplate.queryForObject(
                "select * from posts where id = ?",
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
}
