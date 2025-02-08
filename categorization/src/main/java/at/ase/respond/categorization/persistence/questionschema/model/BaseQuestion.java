package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseQuestion extends Question {

    private int nextBaseQuestionId;

    private List<BaseQuestionField> fields = new ArrayList<>();


    public boolean containsFieldId(String fieldId) {
        return fields.stream().anyMatch(field -> field.getFieldId().equals(fieldId));
    }

    public BaseQuestionField getFieldByFieldId(String fieldId) {
        return fields.stream().filter(field -> field.getFieldId().equals(fieldId)).findFirst().orElse(null);
    }

}
