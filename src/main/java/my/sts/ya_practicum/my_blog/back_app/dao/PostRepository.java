package my.sts.ya_practicum.my_blog.back_app.dao;

import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;

import java.util.List;

public interface PostRepository {

    List<Post> find(PostSearchCriteria searchCriteria, Integer pageNumber, Integer pageSize);
    int count(PostSearchCriteria searchCriteria);
    Post findById(long id);
    Long save(Post post);
    void update(Post post);
    void deletePost(long postId);
    Long incrementLikes(long postId);
}
