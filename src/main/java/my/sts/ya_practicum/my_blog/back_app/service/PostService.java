package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.FindPostsResponseDto;
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

    public FindPostsResponseDto findPosts(
            String search,
            Integer pageNumber,
            Integer pageSize
    ) {
        PostSearchCriteria postSearchCriteria = PostSearchCriteriaParser.parse(search);
        List<Post> posts = postRepository.find(postSearchCriteria, pageNumber, pageSize);
        int postsCount = postRepository.count(postSearchCriteria);

        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        Map<Long, Long> commentsCountMap = commentService.getCommentsCountByPostId(postIds);

        int lastPage = (int) Math.ceil((double) postsCount / pageSize);

        FindPostsResponseDto responseDto = new FindPostsResponseDto();

        responseDto.setPosts(PostDtoMapper.map(posts, commentsCountMap));
        responseDto.setHasPrev(pageNumber > 1);
        responseDto.setHasNext(pageNumber < lastPage);
        responseDto.setLastPage(lastPage);

        return responseDto;
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        Long commentsCount = commentService.getCommentsCountByPostId(List.of(id)).get(id);

        return PostDtoMapper.map(post, commentsCount);
    }

    public PostDto createPost(PostDto postDto) {
        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setTags(postDto.getTags());

        Long postId = postRepository.save(post);

        return findById(postId);
    }

    public boolean exists(Long id) {
        return postRepository.exists(id);
    }

    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId);

        if (post == null) {
            return null;
        }

        post.setText(postDto.getText());
        post.setTitle(postDto.getTitle());
        post.setTags(postDto.getTags());

        postRepository.update(post);

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
