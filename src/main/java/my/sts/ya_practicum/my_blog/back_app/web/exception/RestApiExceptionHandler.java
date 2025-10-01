package my.sts.ya_practicum.my_blog.back_app.web.exception;

import my.sts.ya_practicum.my_blog.back_app.service.exception.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> handleResourceNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
