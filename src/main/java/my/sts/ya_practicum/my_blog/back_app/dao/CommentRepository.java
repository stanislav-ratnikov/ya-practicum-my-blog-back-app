package my.sts.ya_practicum.my_blog.back_app.dao;

import my.sts.ya_practicum.my_blog.back_app.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

    List<Comment> findByPostId(Long postId);
    Map<Long, Long> getCommentsCountByPostId(List<Long> postIds);
}
