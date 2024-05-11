package at.ase.respond.categorization.presentation.dto.questionschema;

import at.ase.respond.categorization.persistence.questionschema.model.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Field.
 *
 * @param fieldId the id of the field
 * @param text    the text of the field
 * @param type    the type of the field
 * @param options the options of the field
 */
@Schema(description = "A DTO representing a field")
public record FieldDTO(String fieldId, String text, FieldType type, List<String> options) implements Serializable {
}
