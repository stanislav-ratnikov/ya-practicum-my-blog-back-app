package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.model.Post;
import my.sts.ya_practicum.my_blog.back_app.util.mapper.FindPostsResponseDtoMapper;
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

    public FindPostsResponseDto findPosts(
            String search,
            Integer pageNumber,
            Integer pageSize
    ) {
        PostSearchCriteria postSearchCriteria = PostSearchCriteriaParser.parse(search);

        List<Post> posts = postRepository.find(postSearchCriteria, pageNumber, pageSize);
        int postsTotalCount = postRepository.countTotalPosts(postSearchCriteria);

        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        Map<Long, Long> postCommentsCounts = commentService.getCommentsCountByPostId(postIds);

        return FindPostsResponseDtoMapper.map(
                posts,
                postCommentsCounts,
                postsTotalCount,
                pageNumber,
                pageSize
        );
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        Long commentsCount = commentService.getCommentsCountByPostId(List.of(id)).get(id);

        return PostDtoMapper.map(post, commentsCount);
    }

    public PostDto createPost(PostDto postDto) {
        Long postId = postRepository.save(PostDtoMapper.map(postDto));

        return findById(postId);
    }

    public boolean exists(Long id) {
        return postRepository.exists(id);
    }

    public PostDto updatePost(Long postId, PostDto postDto) {
        postRepository.update(PostDtoMapper.map(postDto));

        return findById(postId);
    }

    public void deletePost(Long postId) {
        commentService.deleteByPostId(postId);
        postRepository.deletePost(postId);
    }

    public Long incrementLikesCount(Long postId) {
        return postRepository.incrementLikes(postId);
    }
}
