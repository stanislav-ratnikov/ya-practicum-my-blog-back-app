package my.sts.ya_practicum.my_blog.back_app.controller;

import my.sts.ya_practicum.my_blog.back_app.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.service.CommentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentDto> getPostComments(@PathVariable("id") Long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    //temp get rid of UI bug
    @GetMapping(value = "undefined/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentDto> getComments() {
        return Collections.emptyList();
    }
}
