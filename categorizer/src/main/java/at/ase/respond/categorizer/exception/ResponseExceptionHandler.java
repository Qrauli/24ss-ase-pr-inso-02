package at.ase.respond.categorizer.exception;

import at.ase.respond.categorizer.presentation.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(OperationCodeNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(OperationCodeNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(exception.getMessage()));
    }

}
