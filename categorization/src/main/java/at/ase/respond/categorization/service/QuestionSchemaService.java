package at.ase.respond.categorization.service;

import at.ase.respond.categorization.persistence.questionschema.model.Question;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionSchema;

public interface QuestionSchemaService {

    /**
     * Returns the question schema.
     * @return the question schema
     */
    QuestionSchema getQuestionSchema();

    /**
     * Returns the question for the given type, protocolId (if applicable) and id.
     * @param type
     * @param id
     * @param protocolId
     * @return
     */
    Question getQuestionByTypeAndId(QuestionType type, int id, Integer protocolId);

}
