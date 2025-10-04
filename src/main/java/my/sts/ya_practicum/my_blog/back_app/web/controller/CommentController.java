package my.sts.ya_practicum.my_blog.back_app.web.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import my.sts.ya_practicum.my_blog.back_app.service.CommentService;
import my.sts.ya_practicum.my_blog.back_app.web.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.web.validation.CommentRequestValidator;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@Validated
public class CommentController {

    private final CommentService commentService;
    private final CommentRequestValidator commentRequestValidator;

    @Autowired
    public CommentController(CommentService commentService, CommentRequestValidator commentRequestValidator) {
        this.commentService = commentService;
        this.commentRequestValidator = commentRequestValidator;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentDto> getComments(@PathVariable("postId") Long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @GetMapping(value = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDto getComment(
            @PathVariable("postId") @NotNull Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.findComment(postId, commentId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody @Validated({Default.class, Create.class}) CommentDto commentDto
    ) {
        commentRequestValidator.validate(postId, commentDto);

        CommentDto comment = commentService.createComment(postId, commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDto updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody @Validated({Default.class, Update.class}) CommentDto commentDto
    ) {
        commentRequestValidator.validate(postId, commentId, commentDto);

        return commentService.updateComment(postId, commentId, commentDto);
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(postId, commentId);

        return ResponseEntity.noContent().build();
    }
}
