package at.ase.respond.common.dto;

import java.io.Serializable;

/**
 * Represents an error.
 *
 * @param message the error message
 */
public record ErrorDTO(String message) implements Serializable {
}
