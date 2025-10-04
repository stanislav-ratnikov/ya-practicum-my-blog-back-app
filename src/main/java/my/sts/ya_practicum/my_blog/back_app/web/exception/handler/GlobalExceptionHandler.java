package my.sts.ya_practicum.my_blog.back_app.web.exception.handler;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import my.sts.ya_practicum.my_blog.back_app.service.exception.ResourceNotFoundException;
import my.sts.ya_practicum.my_blog.back_app.web.exception.RequestValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ResourceNotFoundException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleResourceNotFoundException(ResourceNotFoundException e) {
        log.info("Resource not found", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RequestValidationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleInvalidRequestException(RequestValidationException e) {
        log.info("Invalid request arguments", e);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        log.info("", ex);

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
