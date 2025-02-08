package at.ase.respond.categorization.persistence.categorization.model;

import at.ase.respond.categorization.persistence.questionschema.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBundle {

    Question question;

    Answer answer;

    // Copy constructor
    public QuestionBundle(QuestionBundle other) {
        this.question = other.question;
        this.answer = other.answer != null ? new Answer(other.answer) : null;
    }

}
