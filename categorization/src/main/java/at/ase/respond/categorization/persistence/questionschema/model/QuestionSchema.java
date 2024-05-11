package at.ase.respond.categorization.persistence.questionschema.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a QuestionSchema.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionSchema {

    /**
     * The version of the schema.
     */
    private String version;

    /**
     * The questions of the schema.
     */
    private Questions questions;

}
