package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.persistence.model.Comment;
import my.sts.ya_practicum.my_blog.back_app.persistence.repository.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.service.exception.InvalidRequestException;
import my.sts.ya_practicum.my_blog.back_app.service.exception.ResourceNotFoundException;
import my.sts.ya_practicum.my_blog.back_app.service.util.mapper.CommentDtoMapper;
import my.sts.ya_practicum.my_blog.back_app.web.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostValidationService postValidationService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostValidationService postValidationService) {
        this.postValidationService = postValidationService;
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> findCommentsByPostId(Long postId) {
        postValidationService.validateIsPostExists(postId);

        return CommentDtoMapper.map(commentRepository.findByPostId(postId));
    }

    public CommentDto findComment(Long postId, Long commentId) {
        postValidationService.validateIsPostExists(postId);

        Comment comment = commentRepository.findByPostIdAndCommentId(postId, commentId);

        if (comment == null) {
            throw new ResourceNotFoundException();
        }

        return CommentDtoMapper.map(comment);
    }

    public CommentDto createComment(Long postId, CommentDto commentDto) {
        if (!Objects.equals(postId, commentDto.getPostId())) {
            throw new InvalidRequestException();
        }

        postValidationService.validateIsPostExists(postId);

        Comment comment = new Comment();

        comment.setPostId(postId);
        comment.setText(commentDto.getText());

        Long commentId = commentRepository.save(comment);

        return findComment(postId, commentId);
    }

    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        if (!Objects.equals(postId, commentDto.getPostId()) || !Objects.equals(commentId,  commentDto.getId())) {
            throw new InvalidRequestException();
        }

        postValidationService.validateIsPostExists(postId);

        Comment comment = commentRepository.findByPostIdAndCommentId(postId, commentId);

        if (comment == null) {
            throw new ResourceNotFoundException();
        }

        comment.setText(commentDto.getText());

        commentRepository.update(comment);

        return findComment(postId, commentId);
    }

    public void deleteComment(Long postId, Long commentId) {
        postValidationService.validateIsPostExists(postId);

        Comment comment = commentRepository.findByPostIdAndCommentId(postId, commentId);

        if (comment == null) {
            throw new ResourceNotFoundException();
        }

        commentRepository.delete(postId, commentId);
    }
}
