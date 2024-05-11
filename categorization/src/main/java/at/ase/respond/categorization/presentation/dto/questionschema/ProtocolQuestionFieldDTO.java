package at.ase.respond.categorization.presentation.dto.questionschema;

import at.ase.respond.categorization.persistence.questionschema.model.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a ProtocolQuestionField.
 *
 * @param fieldId the id of the field
 * @param type    the type of the field
 * @param options the options of the field
 */
@Schema(description = "A DTO representing a protocol question field")
public record ProtocolQuestionFieldDTO(String fieldId, FieldType type,
                                       List<ProtocolQuestionOptionDTO> options) implements Serializable {

}
