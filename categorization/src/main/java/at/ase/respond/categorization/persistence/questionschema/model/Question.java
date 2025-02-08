package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {

    private QuestionType questionType;

    private int id;

    private String text;

}
