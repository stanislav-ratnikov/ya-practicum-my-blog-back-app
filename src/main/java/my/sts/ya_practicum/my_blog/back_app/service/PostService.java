package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.PostDtoMapper;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteriaParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> findPosts(
            String search,
            Integer pageNumber,
            Integer pageSize
    ) {
        PostSearchCriteria postSearchCriteria = PostSearchCriteriaParser.parse(search);
        List<Post> posts = postRepository.find(postSearchCriteria, pageNumber, pageSize);

        return PostDtoMapper.map(posts);
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

    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId);

        if (post == null) {
            return null;
        }

        post.setText(postDto.getText());
        post.setTitle(postDto.getTitle());

        postRepository.update(post);

        return PostDtoMapper.map(post);
    }
}
