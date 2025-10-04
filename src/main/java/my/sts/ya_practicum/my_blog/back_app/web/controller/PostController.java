package my.sts.ya_practicum.my_blog.back_app.web.controller;

import jakarta.validation.groups.Default;
import my.sts.ya_practicum.my_blog.back_app.service.PostService;
import my.sts.ya_practicum.my_blog.back_app.web.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.web.validation.PostRequestValidator;
import my.sts.ya_practicum.my_blog.back_app.web.validation.group.Create;
import my.sts.ya_practicum.my_blog.back_app.web.validation.group.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@Validated
public class PostController {

    private final PostService postService;
    private final PostRequestValidator postRequestValidator;

    @Autowired
    public PostController(PostService postService, PostRequestValidator postRequestValidator) {
        this.postService = postService;
        this.postRequestValidator = postRequestValidator;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FindPostsResponseDto findPosts(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        return postService.findPosts(search, pageNumber, pageSize);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto getPost(@PathVariable("id") Long id) {
        return postService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> createPost(@RequestBody @Validated({Default.class, Create.class}) PostDto post) {
        PostDto result = postService.createPost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto updatePost(
            @PathVariable("id") Long postId,
            @RequestBody @Validated({Default.class, Update.class}) PostDto post
    ) {
        postRequestValidator.validate(postId, post);

        return postService.updatePost(postId, post);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/likes")
    public Long incrementLikes(@PathVariable("id") Long postId) {
        return postService.incrementLikesCount(postId);
    }
}
