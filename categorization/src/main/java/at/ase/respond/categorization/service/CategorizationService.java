package at.ase.respond.categorization.service;

import at.ase.respond.categorization.presentation.dto.AnswerDTO;
import at.ase.respond.categorization.presentation.dto.CategorizationDTO;

import java.util.UUID;

public interface CategorizationService {

    /**
     * Creates a new categorization session.
     *
     * @return the created categorization object with the sessionId and the first question.
     */
    CategorizationDTO createSession();


    /**
     * Saves an answer to a question and returns the updated categorization object.
     * @param sessionId the id of the categorization session
     * @param answerDTO the answer to save
     * @return the updated categorization object
     */
    CategorizationDTO save(UUID sessionId, AnswerDTO answerDTO);

    /**
     * Returns the categorization object with the given categorization sessionId.
     *
     * @param sessionId the id of the categorization session
     * @return the categorization object
     */
    CategorizationDTO findById(UUID sessionId);
}
