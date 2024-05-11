package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.questionschema.model.ProtocolQuestionOption;
import at.ase.respond.categorization.presentation.dto.questionschema.ProtocolQuestionOptionDTO;

public class ProtocolQuestionOptionMapper {

    private ProtocolQuestionOptionMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static ProtocolQuestionOptionDTO toDTO(ProtocolQuestionOption protocolQuestionOption) {
        if (protocolQuestionOption == null) {
            return null;
        }

        return new ProtocolQuestionOptionDTO(protocolQuestionOption.getText());
    }

}
