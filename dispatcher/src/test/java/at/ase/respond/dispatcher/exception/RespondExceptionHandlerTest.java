package at.ase.respond.dispatcher.exception;

import at.ase.respond.common.dto.ErrorDTO;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.common.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RespondExceptionHandlerTest {

    @Autowired
    private RespondExceptionHandler exceptionHandler;

    @Test
    void notFound_shouldBeMappedTo404() {
        ResponseEntity<ErrorDTO> result = exceptionHandler.handleException(new NotFoundException("Not found"));

        assertThat(result.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void validation_shouldBeMappedTo400() {
        ResponseEntity<ErrorDTO> result = exceptionHandler.handleException(new ValidationException("Validation failed"));

        assertThat(result.getStatusCode().value()).isEqualTo(400);
    }

}