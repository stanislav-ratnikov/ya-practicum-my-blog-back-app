package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.persistence.repository.PostRepository;
import my.sts.ya_practicum.my_blog.back_app.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostValidationServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostValidationService postValidationService;

    @Test
    void validateIsPostExists_shouldNotThrowException_whenPostExists() {
        long postId = 1L;

        when(postRepository.exists(postId)).thenReturn(true);

        assertDoesNotThrow(() -> postValidationService.validateIsPostExists(postId));

        verify(postRepository, times(1)).exists(postId);
    }

    @Test
    void validateIsPostExists_shouldThrowResourceNotFoundException_whenPostDoesNotExist() {
        long postId = 1L;

        when(postRepository.exists(postId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> postValidationService.validateIsPostExists(postId));

        verify(postRepository, times(1)).exists(postId);
    }
}
