package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.questionschema.model.BaseQuestion;
import at.ase.respond.categorization.presentation.dto.questionschema.BaseQuestionDTO;

import java.util.stream.Collectors;

public class BaseQuestionMapper {

    private BaseQuestionMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static BaseQuestionDTO toDTO(BaseQuestion baseQuestion) {
        if (baseQuestion == null) {
            return null;
        }

        return new BaseQuestionDTO(baseQuestion.getQuestionType(),
                baseQuestion.getId(),
                baseQuestion.getText(),
                baseQuestion.getFields().stream().map(FieldMapper::toDTO).collect(Collectors.toList())
        );
    }

}
