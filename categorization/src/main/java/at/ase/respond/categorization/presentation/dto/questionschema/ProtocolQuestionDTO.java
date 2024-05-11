package at.ase.respond.categorization.presentation.dto.questionschema;

import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a ProtocolQuestion.
 *
 * @param questionType the type of the question
 * @param id           the id of the question
 * @param text         the text of the question
 * @param protocolId   the id of the protocol. Not used for base questions
 * @param fields       the fields of the question
 */
@Schema(description = "A DTO representing a protocol question")
public record ProtocolQuestionDTO(QuestionType questionType, int id, String text, int protocolId,
                                  List<ProtocolQuestionFieldDTO> fields) implements Serializable {

}
