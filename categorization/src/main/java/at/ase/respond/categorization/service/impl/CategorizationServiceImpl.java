package at.ase.respond.categorization.service.impl;

import at.ase.respond.categorization.exception.NotFoundException;
import at.ase.respond.categorization.persistence.categorization.CategorizationRepository;
import at.ase.respond.categorization.persistence.categorization.model.Answer;
import at.ase.respond.categorization.persistence.categorization.model.Categorization;
import at.ase.respond.categorization.persistence.categorization.model.QuestionBundle;
import at.ase.respond.categorization.persistence.questionschema.model.BaseQuestion;
import at.ase.respond.categorization.persistence.questionschema.model.Question;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import at.ase.respond.categorization.presentation.dto.AnswerDTO;
import at.ase.respond.categorization.presentation.dto.CategorizationDTO;
import at.ase.respond.categorization.presentation.mapper.AnswerMapper;
import at.ase.respond.categorization.presentation.mapper.CategorizationMapper;
import at.ase.respond.categorization.service.CategorizationService;
import at.ase.respond.categorization.service.QuestionSchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategorizationServiceImpl implements CategorizationService {

    private final CategorizationRepository categorizationRepository;

    private final QuestionSchemaService questionSchemaService;

    @Value("${custom.questionschema.protocolSelectorQuestionId}")
    private int protocolSelectorQuestionId;

    /**
     * Creates a new categorization session by generating a random sessionId and adding the first question to the session.
     *
     * @return an initialized Categorization object with a new sessionId and the first question.
     */
    @Override
    public CategorizationDTO createSession() {
        // Create a new Categorization object
        Categorization categorization = new Categorization();

        // Initialization
        categorization.setSessionId(UUID.randomUUID());
        // TODO set the correct user
        categorization.setCreatedBy("admin");
        categorization.setCreatedAt(LocalDateTime.now());

        // Get the first base question
        Question firstBaseQuestion = questionSchemaService.getQuestionSchema().getQuestions().getBaseQuestions().getFirst();
        categorization.addQuestionBundle(new QuestionBundle(firstBaseQuestion, null));

        // Save the document
        categorizationRepository.save(categorization);

        return CategorizationMapper.toDTO(categorization);
    }

    /**
     * Saves the answer to the session and returns the updated categorization object.
     *
     * @param sessionId the ID of the categorization session
     * @param answerDTO the answer to save
     * @return the updated categorization object
     */
    @Override
    public CategorizationDTO save(UUID sessionId, AnswerDTO answerDTO) {
        // Fetch the categorization
        Categorization categorization = categorizationRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("No categorization found with ID: " + sessionId));

        Answer answer = AnswerMapper.toEntity(answerDTO);
        QuestionType questionType = answer.getQuestionType();

        Categorization updatedCategorization = insertAnswer(categorization, answer);

        if (questionType == QuestionType.BASE) {
            BaseQuestion question = (BaseQuestion) questionSchemaService.getQuestionByTypeAndId(questionType, answer.getQuestionId(), null);

            if (question == null) {
                log.error("Base question not found: {}", answer.getQuestionId());
                throw new NotFoundException("Base question not found: " + answer.getQuestionId());
            }

            // Retrieve the next question
            Question nextQuestion;
            if (question.getId() == protocolSelectorQuestionId) {
                // Next question is a protocol question
                String protocolIdString = answer.getAnswers().get("mpdsProtocolId");

                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(protocolIdString);

                if (matcher.find()) {
                    int protocolId = Integer.parseInt(matcher.group());
                    nextQuestion = questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, protocolId);
                } else {
                    log.error("Protocol ID not found in response: {}", protocolIdString);
                    throw new NotFoundException("Protocol ID not found in response: " + protocolIdString);
                }

                if (nextQuestion == null) {
                    log.error("First protocol question not found for protocol: {}", protocolIdString);
                    throw new NotFoundException("First protocol question not found for protocol: " + protocolIdString);
                }
            } else {
                // Next question is a base question
                nextQuestion = questionSchemaService.getQuestionByTypeAndId(questionType, question.getNextBaseQuestionId(), null);

                if (nextQuestion == null) {
                    log.error("Next base question not found. id: {}", question.getNextBaseQuestionId());
                    throw new NotFoundException("Next base question not found: " + question.getNextBaseQuestionId());
                }
            }

            // TODO only add it if it is not already in the list
            updatedCategorization.addQuestionBundle(new QuestionBundle(nextQuestion, null));
        } else if (questionType == QuestionType.PROTOCOL) {
            // Return the next protocol question or dispatch code
            // TODO implement this
            // TODO handle check if it is a dispatch code
        } else {
            log.error("Unknown question type: {}", questionType);
        }

        // Save the document
        updatedCategorization = categorizationRepository.save(updatedCategorization);

        return CategorizationMapper.toDTO(updatedCategorization);
    }

    /**
     * Inserts the answer into the categorization object.
     *
     * @param categorization the categorization object
     * @param answer         the answer to insert
     * @return the updated categorization object
     */
    private Categorization insertAnswer(Categorization categorization, Answer answer) {
        // TODO validate the answer
        // TODO check if the answer is already in the list and check what to do when updating it

        // Update the map
        QuestionBundle questionBundle = categorization.getQuestionBundleByTypeAndId(answer.getQuestionType(), answer.getQuestionId(), answer.getProtocolId());
        questionBundle.setAnswer(answer);

        // Save the document
        return categorizationRepository.save(categorization);
    }

    /**
     * Returns a list of all answers for the categorization with the given categorization sessionId.
     *
     * @param sessionId the id of the categorization session
     * @return a list of all questions with answers for the given session
     */
    @Override
    public CategorizationDTO findById(UUID sessionId) {
        Categorization categorization = categorizationRepository.findById(sessionId).orElseThrow(
                () -> new NotFoundException("No categorization found with ID: " + sessionId)
        );

        return CategorizationMapper.toDTO(categorization);
    }

}
