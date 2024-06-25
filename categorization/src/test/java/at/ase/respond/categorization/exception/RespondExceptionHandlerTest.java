package at.ase.respond.categorization.exception;

import at.ase.respond.categorization.presentation.dto.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RespondExceptionHandlerTest {

    @Autowired
    private RespondExceptionHandler exceptionHandler;

    @Test
    void notFound_shouldBeMappedTo404() {
        ResponseEntity<ErrorDTO> result = exceptionHandler.handleException(new NotFoundException("Not found"));

        assertThat(result.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void badRequest_shouldBeMappedTo400() {
        ResponseEntity<ErrorDTO> result = exceptionHandler.handleException(new BadRequestException("Bad request"));

        assertThat(result.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void invalidQuestionType_shouldBeMappedTo400() {
        ResponseEntity<ErrorDTO> result = exceptionHandler.handleException(new InvalidQuestionTypeException("Invalid question type"));

        assertThat(result.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void validation_shouldBeMappedTo422() {
        ResponseEntity<ErrorDTO> result = exceptionHandler.handleException(new ValidationException("Validation failed"));

        assertThat(result.getStatusCode().value()).isEqualTo(422);
    }

}
