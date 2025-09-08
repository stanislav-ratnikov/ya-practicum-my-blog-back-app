package my.sts.ya_practicum.my_blog.back_app.controller;

import my.sts.ya_practicum.my_blog.back_app.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.service.CommentService;
import my.sts.ya_practicum.my_blog.back_app.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FindPostsResponseDto findPosts(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "pageNumber",required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",required = false) Integer pageSize
    ) {
        FindPostsResponseDto responseDto = new FindPostsResponseDto();

        responseDto.setPosts(postService.findAll());

        return responseDto;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto getPost(@PathVariable("id") Long id) {
        return postService.findById(id);
    }

    @GetMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentDto> getPostComments(@PathVariable("id") Long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getPostImage(@PathVariable("id") Long postId) {
        if (!postService.exists(postId)) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = postService.getImage(postId);

        if (bytes == null || bytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(bytes);
    }
}
