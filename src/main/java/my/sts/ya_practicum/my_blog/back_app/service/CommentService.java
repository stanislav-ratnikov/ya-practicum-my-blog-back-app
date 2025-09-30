package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.persistence.repository.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.web.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.persistence.model.Comment;
import my.sts.ya_practicum.my_blog.back_app.service.util.mapper.CommentDtoMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> findCommentsByPostId(Long postId) {
        if (postId == null) {
            return Collections.emptyList();
        }

        return CommentDtoMapper.map(commentRepository.findByPostId(postId));
    }

    public Map<Long, Long> getCommentsCountByPostId(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return commentRepository.getCommentsCountByPostId(postIds);
    }

    public void deleteByPostId(Long postId) {
        if (postId == null) {
            return;
        }

        commentRepository.deleteByPostId(postId);
    }

    public CommentDto findComment(Long postId, Long commentId) {
        if (postId == null) {
            return null;
        }

        return CommentDtoMapper.map(commentRepository.findByPostIdAndCommentId(postId, commentId));
    }

    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setPostId(postId);
        comment.setText(commentDto.getText());

        Long commentId = commentRepository.save(comment);

        return findComment(postId, commentId);
    }

    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByPostIdAndCommentId(postId, commentId);

        if (comment == null) {
            return null;
        }

        comment.setText(commentDto.getText());

        commentRepository.update(comment);

        return findComment(postId, commentId);
    }

    public void deleteComment(Long postId, Long commentId) {
        commentRepository.delete(postId, commentId);
    }
}
