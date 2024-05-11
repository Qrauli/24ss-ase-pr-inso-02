package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Questions {

    private List<BaseQuestion> baseQuestions = new ArrayList<>();

    private Map<Integer, Protocol> protocols = new HashMap<>();

}
