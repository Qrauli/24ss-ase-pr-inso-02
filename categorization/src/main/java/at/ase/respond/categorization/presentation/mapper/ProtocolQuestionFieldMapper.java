package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.questionschema.model.ProtocolQuestionField;
import at.ase.respond.categorization.presentation.dto.questionschema.ProtocolQuestionFieldDTO;

import java.util.stream.Collectors;

public class ProtocolQuestionFieldMapper {

    private ProtocolQuestionFieldMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static ProtocolQuestionFieldDTO toDTO(ProtocolQuestionField protocolQuestionField) {
        if (protocolQuestionField == null) {
            return null;
        }

        return new ProtocolQuestionFieldDTO(protocolQuestionField.getFieldId(),
                protocolQuestionField.getType(),
                protocolQuestionField.getOptions().stream().map(ProtocolQuestionOptionMapper::toDTO).collect(Collectors.toList()));
    }

}
