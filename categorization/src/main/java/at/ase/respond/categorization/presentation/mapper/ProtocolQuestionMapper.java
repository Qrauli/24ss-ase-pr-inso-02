package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.questionschema.model.ProtocolQuestion;
import at.ase.respond.categorization.presentation.dto.questionschema.ProtocolQuestionDTO;

import java.util.stream.Collectors;

public class ProtocolQuestionMapper {

    private ProtocolQuestionMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static ProtocolQuestionDTO toDTO(ProtocolQuestion protocolQuestion) {
        if (protocolQuestion == null) {
            return null;
        }

        return new ProtocolQuestionDTO(protocolQuestion.getQuestionType(),
                protocolQuestion.getId(),
                protocolQuestion.getText(),
                protocolQuestion.getProtocolId(),
                protocolQuestion.getFields().stream().map(ProtocolQuestionFieldMapper::toDTO).collect(Collectors.toList()));
    }

}
