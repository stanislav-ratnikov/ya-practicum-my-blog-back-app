package my.sts.ya_practicum.my_blog.back_app.dao;

import my.sts.ya_practicum.my_blog.back_app.model.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> findByPostId(Long postId);
}
