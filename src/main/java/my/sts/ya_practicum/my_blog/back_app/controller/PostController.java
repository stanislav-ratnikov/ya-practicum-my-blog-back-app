package my.sts.ya_practicum.my_blog.back_app.controller;

import my.sts.ya_practicum.my_blog.back_app.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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
    public PostDto createPost(@RequestBody PostDto post) {
        return postService.createPost(post);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto updatePost(@PathVariable("id") Long postId, @RequestBody PostDto post) {
        return postService.updatePost(postId, post);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);

        return ResponseEntity.ok("ok");
    }
}
