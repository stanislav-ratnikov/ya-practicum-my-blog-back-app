package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.CommentRepository;
import my.sts.ya_practicum.my_blog.back_app.dto.CommentDto;
import my.sts.ya_practicum.my_blog.back_app.util.CommentDtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> findCommentsByPostId(Long postId) {
        return CommentDtoMapper.map(commentRepository.findByPostId(postId));
    }
}
