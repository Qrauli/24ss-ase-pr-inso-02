package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.categorization.model.Answer;
import at.ase.respond.categorization.presentation.dto.AnswerDTO;

public class AnswerMapper {

    private AnswerMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static AnswerDTO toDTO(Answer answer) {
        if (answer == null) {
            return null;
        }

        return new AnswerDTO(answer.getQuestionType(),
                answer.getQuestionId(),
                answer.getProtocolId(),
                answer.getAnswers());
    }

    public static Answer toEntity(AnswerDTO answerDTO) {
        if (answerDTO == null) {
            return null;
        }

        return new Answer(answerDTO.questionType(),
                answerDTO.questionId(),
                answerDTO.protocolId(),
                answerDTO.answers());
    }
}
