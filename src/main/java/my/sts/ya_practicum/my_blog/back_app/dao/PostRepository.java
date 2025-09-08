package my.sts.ya_practicum.my_blog.back_app.dao;

import my.sts.ya_practicum.my_blog.back_app.model.Post;

import java.util.List;

public interface PostRepository {

    List<Post> findAll();
    Post findById(long id);
}
