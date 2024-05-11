package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.Data;

@Data
public class ProtocolQuestionOption {

    private String text;

    private String dispatchCode;

    private NextProtocolQuestion nextProtocolQuestion;

}
