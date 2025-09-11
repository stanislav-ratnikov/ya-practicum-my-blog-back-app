package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.mapper.PostDtoMapper;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteria;
import my.sts.ya_practicum.my_blog.back_app.util.search.PostSearchCriteriaParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
    }

    public List<PostDto> findPosts(
            String search,
            Integer pageNumber,
            Integer pageSize
    ) {
        PostSearchCriteria postSearchCriteria = PostSearchCriteriaParser.parse(search);
        List<Post> posts = postRepository.find(postSearchCriteria, pageNumber, pageSize);

        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        Map<Long, Long> commentCounts = commentService.getCommentCounts(postIds);

        return PostDtoMapper.map(posts, commentCounts);
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        Long commentCount = commentService.getCommentCounts(List.of(id)).get(id);

        return PostDtoMapper.map(post, commentCount);
    }

    public PostDto createPost(PostDto postDto) {
        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());

        Long postId = postRepository.save(post);

        post.setId(postId);

        return PostDtoMapper.map(post, /*todo:*/ 0L);
    }

    public boolean exists(Long id) {
        return true;
    }

    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId);

        if (post == null) {
            return null;
        }

        post.setText(postDto.getText());
        post.setTitle(postDto.getTitle());

        postRepository.update(post);

        return PostDtoMapper.map(post, /*todo:*/ 0L);
    }
}
