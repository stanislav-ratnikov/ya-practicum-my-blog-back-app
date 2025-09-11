package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.util.mapper.CommentDtoMapper;
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

    public Map<Long, Long> getCommentCounts(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return commentRepository.getCommentCounts(postIds);
    }
}
