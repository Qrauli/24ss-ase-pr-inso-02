package at.ase.respond.categorization.presentation.dto.questionschema;

import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a BaseQuestion.
 *
 * @param questionType is in this case always BASE
 * @param id the id of the base question
 * @param text the text of the base question
 * @param fields the fields of the base question
 */
@Schema(description = "A DTO representing a base question")
public record BaseQuestionDTO(QuestionType questionType, int id, String text,
                              List<FieldDTO> fields) implements Serializable {

}
