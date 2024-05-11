package at.ase.respond.categorization.persistence.categorization.model;

import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    private QuestionType questionType;

    private int questionId;

    private int protocolId;

    private Map<String, String> answers = new HashMap<>();

}
