package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.questionschema.model.BaseQuestionField;
import at.ase.respond.categorization.presentation.dto.questionschema.FieldDTO;

public class FieldMapper {

    private FieldMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static FieldDTO toDTO(BaseQuestionField baseQuestionField) {
        if (baseQuestionField == null) {
            return null;
        }

        return new FieldDTO(baseQuestionField.getFieldId(),
                baseQuestionField.getText(),
                baseQuestionField.getType(),
                baseQuestionField.getOptions());
    }

}
