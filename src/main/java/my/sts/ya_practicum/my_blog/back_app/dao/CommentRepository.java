package my.sts.ya_practicum.my_blog.back_app.dao;

import my.sts.ya_practicum.my_blog.back_app.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

    List<Comment> findByPostId(Long postId);
    Comment findByPostIdAndCommentId(Long postId, Long commentId);
    Map<Long, Long> getCommentsCountByPostId(List<Long> postIds);
    void deleteByPostId(Long postId);
    Long save(Comment comment);
    void update(Comment comment);
}
