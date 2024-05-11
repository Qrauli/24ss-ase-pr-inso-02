package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.categorization.model.QuestionBundle;
import at.ase.respond.categorization.persistence.questionschema.model.BaseQuestion;
import at.ase.respond.categorization.persistence.questionschema.model.ProtocolQuestion;
import at.ase.respond.categorization.presentation.dto.QuestionBundleDTO;

public class QuestionBundleMapper {

    private QuestionBundleMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static QuestionBundleDTO toDTO(QuestionBundle questionBundle) {
        if (questionBundle == null) {
            return null;
        }

        QuestionBundleDTO questionBundleDTO = new QuestionBundleDTO();
        if (questionBundle.getQuestion() instanceof BaseQuestion) {
            questionBundleDTO.setBaseQuestion(BaseQuestionMapper.toDTO((BaseQuestion) questionBundle.getQuestion()));
        } else if (questionBundle.getQuestion() instanceof ProtocolQuestion) {
            questionBundleDTO.setProtocolQuestion(ProtocolQuestionMapper.toDTO((ProtocolQuestion) questionBundle.getQuestion()));
        } else {
            throw new IllegalArgumentException("Unsupported question type");
        }

        questionBundleDTO.setAnswer(AnswerMapper.toDTO(questionBundle.getAnswer()));

        return questionBundleDTO;
    }

}
