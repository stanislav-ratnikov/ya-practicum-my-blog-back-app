package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.persistence.repository.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostValidationService {

    private final PostRepository postRepository;

    @Autowired
    public PostValidationService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void validateIsPostExists(Long postId) {
        if (!postRepository.exists(postId)) {
            throw new ResourceNotFoundException();
        }
    }
}
