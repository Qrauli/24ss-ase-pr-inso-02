package at.ase.respond.datafeeder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import at.ase.respond.common.dto.ErrorDTO;
import at.ase.respond.common.exception.NotFoundException;

@ControllerAdvice
public class RespondExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

}
