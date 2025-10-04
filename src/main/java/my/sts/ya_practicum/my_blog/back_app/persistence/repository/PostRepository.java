package my.sts.ya_practicum.my_blog.back_app.persistence.repository;

import my.sts.ya_practicum.my_blog.back_app.persistence.model.Post;
import my.sts.ya_practicum.my_blog.back_app.service.search.PostSearchCriteria;

import java.util.List;

public interface PostRepository {

    List<Post> find(PostSearchCriteria searchCriteria, Integer pageNumber, Integer pageSize);
    int countTotalPosts(PostSearchCriteria searchCriteria);
    Post findById(long id);
    Long save(Post post);
    void update(Post post);
    void deletePost(long postId);
    Long incrementLikes(long postId);
    boolean exists(long id);
}
