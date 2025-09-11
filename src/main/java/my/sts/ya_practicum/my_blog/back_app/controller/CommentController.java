package my.sts.ya_practicum.my_blog.back_app.controller;

import my.sts.ya_practicum.my_blog.back_app.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.service.CommentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentDto> getPostComments(@PathVariable("postId") Long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @GetMapping(value = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDto getComments(@PathVariable("postId") Long postId,
                                  @PathVariable("commentId") Long commentId
    ) {
        return commentService.findComment(postId, commentId);
    }
}
