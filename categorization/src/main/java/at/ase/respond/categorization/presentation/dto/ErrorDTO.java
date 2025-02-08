package at.ase.respond.categorization.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents an error.
 *
 * @param message the error message
 */
@Schema(description = "A DTO representing an error")
public record ErrorDTO(String message) {
}
