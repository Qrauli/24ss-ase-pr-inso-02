package at.ase.respond.categorization.persistence.questionschema.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Protocol {

    private String title;

    private String description;

    private List<ProtocolQuestion> questions = new ArrayList<>();

}
