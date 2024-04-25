package at.ase.respond.categorizer.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a categorization.
 *
 * @param id the id of the questionary where the categorization was derived
 * @param code the resulting categorization code
 */
@Schema(description = "A DTO representing a categorization")
public record CategorizationDTO(UUID id, String code) implements Serializable {
}
