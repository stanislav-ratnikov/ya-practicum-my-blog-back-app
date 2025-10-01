package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.persistence.repository.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.persistence.repository.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.service.exception.InvalidRequestException;
import my.sts.ya_practicum.my_blog.back_app.service.exception.ResourceNotFoundException;
import my.sts.ya_practicum.my_blog.back_app.web.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.persistence.model.Post;
import my.sts.ya_practicum.my_blog.back_app.service.util.mapper.FindPostsResponseDtoMapper;
import my.sts.ya_practicum.my_blog.back_app.service.util.mapper.PostDtoMapper;
import my.sts.ya_practicum.my_blog.back_app.service.util.search.PostSearchCriteria;
import my.sts.ya_practicum.my_blog.back_app.service.util.search.PostSearchCriteriaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostValidationService postValidationService;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(
            PostRepository postRepository,
            PostValidationService postValidationService,
            CommentRepository commentRepository
    ) {
        this.postRepository = postRepository;
        this.postValidationService = postValidationService;
        this.commentRepository = commentRepository;
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

        Map<Long, Long> postCommentsCounts = !postIds.isEmpty()
                ? commentRepository.getCommentsCountByPostId(postIds)
                : Collections.emptyMap();

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

        if (post == null) {
            throw new ResourceNotFoundException();
        }

        Long commentsCount = commentRepository.getCommentsCountByPostId(List.of(id)).get(id);

        return PostDtoMapper.map(post, commentsCount);
    }

    public PostDto createPost(PostDto postDto) {
        Long postId = postRepository.save(PostDtoMapper.map(postDto));

        return findById(postId);
    }

    public PostDto updatePost(Long postId, PostDto postDto) {
        if (!Objects.equals(postId, postDto.getId())) {
            throw new InvalidRequestException();
        }

        postValidationService.validateIsPostExists(postId);
        postRepository.update(PostDtoMapper.map(postDto));

        return findById(postId);
    }

    public void deletePost(Long postId) {
        postValidationService.validateIsPostExists(postId);
        commentRepository.deleteByPostId(postId);
        postRepository.deletePost(postId);
    }

    public Long incrementLikesCount(Long postId) {
        postValidationService.validateIsPostExists(postId);

        return postRepository.incrementLikes(postId);
    }
}
