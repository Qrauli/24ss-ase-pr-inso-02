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

}
