package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.PostDtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> findAll() {
        return PostDtoMapper.map(postRepository.findAll());
    }

    public PostDto findById(Long id) {
        return PostDtoMapper.map(postRepository.findById(id));
    }

    public PostDto createPost(PostDto postDto) {
        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());

        Long postId = postRepository.save(post);

        post.setId(postId);

        return PostDtoMapper.map(post);
    }

    public boolean exists(Long id) {
        return true;
    }

    public byte[] getImage(Long postId) {
        return new byte[0];
    }
}
