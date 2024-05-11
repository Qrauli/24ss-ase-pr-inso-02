package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolQuestionField {

    private String fieldId;

    private FieldType type;

    private List<ProtocolQuestionOption> options = new ArrayList<>();

}
