package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseQuestionField {

    private String fieldId;

    private String text;

    private FieldType type;

    private List<String> options = new ArrayList<>();

}
