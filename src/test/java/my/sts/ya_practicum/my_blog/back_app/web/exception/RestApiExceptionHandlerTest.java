package my.sts.ya_practicum.my_blog.back_app.web.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class RestApiExceptionHandlerTest {

    private final RestApiExceptionHandler restApiExceptionHandler = new RestApiExceptionHandler();

    @Test
    void handleResourceNotFoundException_shouldReturnResponseEntity_withNotFoundStatusCode() {
        ResponseEntity<Void> response = restApiExceptionHandler.handleResourceNotFoundException();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
