package at.ase.respond.categorization.presentation.dto.questionschema;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents a ProtocolQuestionOption.
 *
 * @param text the text of the option
 */
@Schema(description = "A DTO representing a protocol question option")
public record ProtocolQuestionOptionDTO(String text) implements Serializable {

}
