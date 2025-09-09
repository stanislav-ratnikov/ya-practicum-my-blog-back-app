package my.sts.ya_practicum.my_blog.back_app.dao;

import my.sts.ya_practicum.my_blog.back_app.model.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> findByPostId(Long postId);
    List<Tag> findByPostIds(List<Long> postIds);
}
