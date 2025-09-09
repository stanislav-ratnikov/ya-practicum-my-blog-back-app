package my.sts.ya_practicum.my_blog.back_app.dao.impl;

import my.sts.ya_practicum.my_blog.back_app.dao.TagRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Tag;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TagRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Tag> findByPostId(Long postId) {
        Map<String, Object> params = new HashMap<>();

        params.put("postId", postId);

        return namedParameterJdbcTemplate.query(
                "select * from tags where post_id = :postId",
                params,
                (rs, rowNum) -> {
                    Tag tag = new Tag();

                    tag.setId(rs.getLong("id"));
                    tag.setName(rs.getString("tag"));
                    tag.setPostId(rs.getLong("post_id"));

                    return tag;
                }
        );
    }

    @Override
    public List<Tag> findByPostIds(List<Long> postIds) {
        Map<String, Object> params = new HashMap<>();

        params.put("postIds", postIds);

        return namedParameterJdbcTemplate.query(
                "select * from tags where post_id IN (:postIds)",
                params,
                (rs, rowNum) -> {
                    Tag tag = new Tag();

                    tag.setId(rs.getLong("id"));
                    tag.setName(rs.getString("tag"));
                    tag.setPostId(rs.getLong("post_id"));

                    return tag;
                }
        );
    }
}
