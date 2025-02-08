package at.ase.respond.categorization.presentation.dto;

import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents an Answer.
 *
 * @param questionType the type of the question
 * @param questionId   the id of the question
 * @param protocolId   the id of the protocol
 * @param answers      a Map containing the answers of the question with <fieldId, value>
 */
@Schema(description = "A DTO representing an answer")
public record AnswerDTO(QuestionType questionType, int questionId, int protocolId,
                        Map<String, String> answers) implements Serializable {

}