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


    public boolean containsFieldId(String fieldId) {
        return fields.stream().anyMatch(field -> field.getFieldId().equals(fieldId));
    }

    public ProtocolQuestionField getFieldByFieldId(String fieldId) {
        return fields.stream().filter(field -> field.getFieldId().equals(fieldId)).findFirst().orElse(null);
    }

    public boolean answerLeadsToDispatchCode(String answer) {
        return fields.stream()
                .flatMap(field -> field.getOptions().stream())
                .anyMatch(option -> option.getText().equals(answer) && option.getDispatchCode() != null);
    }

    public String getDispatchCodeForAnswer(String answer) {
        return fields.stream()
                .flatMap(field -> field.getOptions().stream())
                .filter(option -> option.getText().equals(answer) && option.getDispatchCode() != null)
                .findFirst()
                .map(ProtocolQuestionOption::getDispatchCode)
                .orElse(null);
    }

    public NextProtocolQuestion getNextProtocolQuestionForAnswer(String answer) {
        return fields.stream()
                .flatMap(field -> field.getOptions().stream())
                .filter(option -> option.getText().equals(answer) && option.getNextProtocolQuestion() != null)
                .findFirst()
                .map(ProtocolQuestionOption::getNextProtocolQuestion)
                .orElse(null);
    }

}
