package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProtocolQuestion extends Question {

    private int protocolId;

    private List<ProtocolQuestionField> fields = new ArrayList<>();

}
