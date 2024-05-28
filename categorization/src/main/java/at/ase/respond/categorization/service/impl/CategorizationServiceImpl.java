package at.ase.respond.categorization.service.impl;

import at.ase.respond.categorization.exception.BadRequestException;
import at.ase.respond.categorization.exception.InvalidQuestionTypeException;
import at.ase.respond.categorization.exception.NotFoundException;
import at.ase.respond.categorization.exception.ValidationException;
import at.ase.respond.categorization.persistence.categorization.CategorizationRepository;
import at.ase.respond.categorization.persistence.categorization.model.Answer;
import at.ase.respond.categorization.persistence.categorization.model.Categorization;
import at.ase.respond.categorization.persistence.categorization.model.QuestionBundle;
import at.ase.respond.categorization.persistence.questionschema.model.*;
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

/**
 * Service implementation for managing the categorization sessions.
 * This service provides methods to create, save, and find categorization sessions.
 */
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
     * @return an initialized CategorizationDTO object with a new sessionId and the first question.
     */
    @Override
    public CategorizationDTO createSession() {
        Categorization categorization = new Categorization();

        // Initialization
        categorization.setSessionId(UUID.randomUUID());
        // TODO set the correct user
        categorization.setCreatedBy("admin");
        categorization.setCreatedAt(LocalDateTime.now());

        // Get the first base question
        Question firstBaseQuestion = questionSchemaService.getQuestionSchema()
                .getQuestions()
                .getBaseQuestions()
                .getFirst();

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
     * @return the updated CategorizationDTO object
     * @throws NotFoundException if no categorization is found with the given sessionId
     */
    @Override
    public CategorizationDTO save(UUID sessionId, AnswerDTO answerDTO) throws NotFoundException {
        // Fetch the categorization
        Categorization categorization = categorizationRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("No categorization found with ID: " + sessionId));

        Answer answer = AnswerMapper.toEntity(answerDTO);
        Categorization updatedCategorization = validateAndInsertAnswer(new Categorization(categorization), answer);

        handleQuestionFlow(categorization, updatedCategorization, answer);

        updatedCategorization = categorizationRepository.save(updatedCategorization);
        return CategorizationMapper.toDTO(updatedCategorization);
    }

    /**
     * Manages the flow of questions based on the type of the question (BASE or PROTOCOL).
     * <p>
     * Set answer of the question.
     * Was the answer previously set?
     * If it is a BaseQuestion except the protocol selector question -> do nothing and return everything as it is.
     * Else: Check previous answer.
     *  Did anything change?
     *      Yes -> Was the question the BaseQuestion protocol selector question?
     *              Yes -> check the new answer (for base question protocol selector question)
     *                  -> delete all QuestionBundles of the ProtocolBasedQuestions
     *                  -> get the first protocol question of the new protocol
     *              No -> check the new answer (for protocol question)
     *                      -> if it is a dispatch code, set the dispatch code
     *                      -> if the next question is set, return the next protocol question and delete all QuestionBundles after the current one, get the next question
     *      No -> check if it has to return the dispatch code or return the next protocol question.
     *
     * @param categorization        the current categorization session (before inserting and saving the new answer)
     * @param updatedCategorization the updated categorization session (with the new answer inserted and saved
     * @param answer                the answer provided by the user
     * @throws InvalidQuestionTypeException if an unknown question type is encountered or a required question is not found
     */
    private void handleQuestionFlow(Categorization categorization, Categorization updatedCategorization, Answer answer) throws InvalidQuestionTypeException {
        QuestionType questionType = answer.getQuestionType();

        if (questionType == QuestionType.BASE) {
            handleBaseQuestionFlow(categorization, updatedCategorization, answer);
        } else if (questionType == QuestionType.PROTOCOL) {
            handleProtocolQuestionFlow(categorization, updatedCategorization, answer);
        } else {
            log.error("Unknown question type: {}", questionType);
            throw new InvalidQuestionTypeException("Unknown question type: " + questionType);
        }
    }

    /**
     * Handles the flow of base questions, including the protocol selector question.
     *
     * @param categorization        the current categorization session
     * @param updatedCategorization the updated categorization session
     * @param answer                the answer provided by the user
     * @throws NotFoundException if the next base question is not found, if the required question bundle is not found, or if the next base question is not found
     */
    private void handleBaseQuestionFlow(Categorization categorization, Categorization updatedCategorization, Answer answer) throws NotFoundException {
        BaseQuestion question = (BaseQuestion) questionSchemaService.getQuestionByTypeAndId(answer.getQuestionType(), answer.getQuestionId(), null);

        if (question == null) {
            log.error("Base question not found: {}", answer.getQuestionId());
            throw new NotFoundException("Base question not found: " + answer.getQuestionId());
        }

        // Retrieve the next question
        Question nextQuestion;
        if (question.getId() == protocolSelectorQuestionId) {
            // Next question is a protocol question
            nextQuestion = getFirstProtocolQuestionFromAnswer(answer);

            // Check if answer was already set previously and if the new value is different. If so, delete all protocol questions and set recommendedDispatchCode to null
            QuestionBundle beforeQuestionBundle = categorization.getQuestionBundleByTypeAndId(question.getQuestionType(), question.getId(), null);

            if (beforeQuestionBundle == null) {
                log.error("Question bundle not found for base questionId: {}", answer.getQuestionId());
                throw new NotFoundException("Question bundle not found for questionId: " + answer.getQuestionId() + ", protocolId: " + answer.getProtocolId());
            }

            if (beforeQuestionBundle.getAnswer() != null &&
                    !beforeQuestionBundle.getAnswer().getAnswers().values().iterator().next().equals(answer.getAnswers().values().iterator().next())) {
                updatedCategorization.getQuestionBundles().removeIf(bundle -> bundle.getQuestion().getQuestionType() == QuestionType.PROTOCOL);
                updatedCategorization.setRecommendedDispatchCode(null);
            }
        } else {
            // Next question is a base question
            nextQuestion = questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, question.getNextBaseQuestionId(), null);
        }

        if (nextQuestion == null) {
            log.error("Next base question not found for question ID: {}", question.getNextBaseQuestionId());
            throw new NotFoundException("Next base question not found for question ID: " + question.getNextBaseQuestionId());
        }

        boolean isNextQuestionAlreadyInList = updatedCategorization.containsQuestionBundleByTypeAndId(nextQuestion.getQuestionType(), nextQuestion.getId(), null);

        if (!isNextQuestionAlreadyInList) {
            updatedCategorization.addQuestionBundle(new QuestionBundle(nextQuestion, null));
        }
    }

    /**
     * Retrieves the first protocol question from the answer of the protocol selector base question.
     *
     * @param answer the answer provided by the user
     * @return the first protocol question based on the answer
     * @throws NotFoundException if the protocol ID is not found in the answer or the first protocol question is not found
     */
    private Question getFirstProtocolQuestionFromAnswer(Answer answer) throws NotFoundException {
        String protocolIdString = answer.getAnswers().get("mpdsProtocolId");
        Matcher matcher = Pattern.compile("\\d+").matcher(protocolIdString);

        if (matcher.find()) {
            int protocolId = Integer.parseInt(matcher.group());
            Question nextQuestion = questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, protocolId);
            if (nextQuestion == null) {
                log.error("First protocol question not found for protocol: {}", protocolIdString);
                throw new NotFoundException("First protocol question not found for protocol: " + protocolIdString);
            }
            return nextQuestion;
        } else {
            log.error("Protocol ID not found in response: {}", protocolIdString);
            throw new NotFoundException("Protocol ID not found in response: " + protocolIdString);
        }
    }

    /**
     * Handles the flow of protocol questions, including setting the dispatch code or moving to the next protocol question.
     *
     * @param categorization        the current categorization session
     * @param updatedCategorization the updated categorization session
     * @param answer                the answer provided by the user
     * @throws NotFoundException if the current protocol question is not found
     */
    private void handleProtocolQuestionFlow(Categorization categorization, Categorization updatedCategorization, Answer answer) throws NotFoundException {
        // Get current protocol question
        ProtocolQuestion question = (ProtocolQuestion) questionSchemaService.getQuestionByTypeAndId(answer.getQuestionType(), answer.getQuestionId(), answer.getProtocolId());

        if (question == null) {
            log.error("Protocol question not found. protocolId: {}, questionId: {}", answer.getProtocolId(), answer.getQuestionId());
            throw new NotFoundException("Protocol question not found. protocolId: " + answer.getProtocolId() + ", questionId: " + answer.getQuestionId());
        }

        // Check if the answer leads to a dispatch code
        if (question.answerLeadsToDispatchCode(answer.getAnswers().values().iterator().next())) {
            handleDispatchCodeQuestion(categorization, updatedCategorization, question, answer);
        } else {
            handleNextProtocolQuestion(categorization, updatedCategorization, question, answer);
        }
    }

    /**
     * Handles the dispatch code question, including updating the recommended dispatch code (if needed) and removing subsequent protocol questions
     * if the answer has changed (if the questionnaire tree branch has changed).
     *
     * @param categorization        the current categorization session
     * @param updatedCategorization the updated categorization session
     * @param question              the protocol question being handled
     * @param answer                the answer provided by the user
     * @throws NotFoundException if the question bundle for the answer is not found
     */
    private void handleDispatchCodeQuestion(Categorization categorization, Categorization updatedCategorization, ProtocolQuestion question, Answer answer) throws NotFoundException {
        log.debug("Handling dispatch code question. questionId: {}", question.getId());

        String dispatchCode = question.getDispatchCodeForAnswer(answer.getAnswers().values().iterator().next());
        updatedCategorization.setRecommendedDispatchCode(dispatchCode);

        QuestionBundle beforeQuestionBundle = categorization.getQuestionBundleByTypeAndId(question.getQuestionType(), question.getId(), question.getProtocolId());

        if (beforeQuestionBundle == null) {
            log.error("Question bundle not found for questionId: {}, protocolId: {}", answer.getQuestionId(), answer.getProtocolId());
            throw new NotFoundException("Question bundle not found for questionId: " + answer.getQuestionId() + ", protocolId: " + answer.getProtocolId());
        }

        // Check if the answer was already set previously and if the new value is different
        if (beforeQuestionBundle.getAnswer() != null &&
                !beforeQuestionBundle.getAnswer().getAnswers().values().iterator().next().equals(answer.getAnswers().values().iterator().next())) {
            // Delete all question bundles of protocol questions after the current one
            updatedCategorization.getQuestionBundles().removeIf(bundle -> bundle.getQuestion().getQuestionType() == QuestionType.PROTOCOL &&
                    ((ProtocolQuestion) bundle.getQuestion()).getProtocolId() == answer.getProtocolId() &&
                    bundle.getQuestion().getId() > answer.getQuestionId());
        }
    }

    /**
     * Handles the next protocol question, including fetching and adding it to the updated categorization if it is not already present.
     *
     * @param categorization        the current categorization session
     * @param updatedCategorization the updated categorization session
     * @param question              the protocol question being handled
     * @param answer                the answer provided by the user
     * @throws NotFoundException if the next protocol question is not found or if the current question bundle is not found
     */
    private void handleNextProtocolQuestion(Categorization categorization, Categorization updatedCategorization, ProtocolQuestion question, Answer answer) throws NotFoundException {
        QuestionBundle beforeQuestionBundle = categorization.getQuestionBundleByTypeAndId(question.getQuestionType(), question.getId(), question.getProtocolId());

        if (beforeQuestionBundle == null) {
            log.error("Question bundle not found for questionId: {}, protocolId: {}", answer.getQuestionId(), answer.getProtocolId());
            throw new NotFoundException("Question bundle not found for questionId: " + answer.getQuestionId() + ", protocolId: " + answer.getProtocolId());
        }

        // Check if the answer was already set previously and if the new value is different
        if (beforeQuestionBundle.getAnswer() != null &&
                !beforeQuestionBundle.getAnswer().getAnswers().values().iterator().next().equals(answer.getAnswers().values().iterator().next())) {
            // Delete all question bundles of protocol questions after the current one
            updatedCategorization.getQuestionBundles().removeIf(bundle -> bundle.getQuestion().getQuestionType() == QuestionType.PROTOCOL &&
                    ((ProtocolQuestion) bundle.getQuestion()).getProtocolId() == answer.getProtocolId() &&
                    bundle.getQuestion().getId() > answer.getQuestionId());

            // Set recommendedDispatchCode to null
            updatedCategorization.setRecommendedDispatchCode(null);
        }

        // Fetch the next protocol question
        NextProtocolQuestion nextProtocolQuestionId = question.getNextProtocolQuestionForAnswer(answer.getAnswers().values().iterator().next());
        if (nextProtocolQuestionId == null) {
            log.error("Next protocol question not found for answer: {}", answer.getAnswers().values().iterator().next());
            throw new NotFoundException("Next protocol question not found for answer: " + answer.getAnswers().values().iterator().next());
        }

        ProtocolQuestion nextQuestion = (ProtocolQuestion) questionSchemaService.getQuestionByTypeAndId(
                QuestionType.PROTOCOL, nextProtocolQuestionId.getQuestionId(), nextProtocolQuestionId.getProtocolId());

        if (nextQuestion == null) {
            log.error("Next protocol question not found. protocolId: {}, questionId: {}", nextProtocolQuestionId.getProtocolId(), nextProtocolQuestionId.getQuestionId());
            throw new NotFoundException("Next protocol question not found. protocolId: " + nextProtocolQuestionId.getProtocolId() + ", questionId: " + nextProtocolQuestionId.getQuestionId());
        }

        boolean isNextProtocolQuestionAlreadyInList = updatedCategorization.getQuestionBundles().stream()
                .filter(bundle -> bundle.getQuestion().getQuestionType() == QuestionType.PROTOCOL)
                .anyMatch(bundle -> ((ProtocolQuestion) bundle.getQuestion()).getProtocolId() == nextQuestion.getProtocolId() &&
                        bundle.getQuestion().getId() == nextQuestion.getId());

        if (!isNextProtocolQuestionAlreadyInList) {
            updatedCategorization.addQuestionBundle(new QuestionBundle(nextQuestion, null));
        }
    }

    /**
     * Validates and inserts an answer into the categorization object, ensuring that the corresponding question and fields exist
     * and are valid according to the specific types and rules. If the question or any required field is not found, or if the
     * field type is unsupported, this method logs an error and throws a NotFoundException.
     *
     * @param categorization the current categorization object to be updated with the answer
     * @param answer         the answer to validate and insert
     * @return the updated categorization object
     * @throws NotFoundException if the question bundle for the answer is not found
     */
    private Categorization validateAndInsertAnswer(Categorization categorization, Answer answer) throws NotFoundException, BadRequestException {
        QuestionBundle questionBundle = categorization.getQuestionBundleByTypeAndId(answer.getQuestionType(), answer.getQuestionId(), answer.getProtocolId());

        if (questionBundle == null) {
            log.error("Question bundle to answer not found in categorization: QuestionType: {}, ProtocolId: {}, QuestionId: {}", answer.getQuestionType(), answer.getProtocolId(), answer.getQuestionId());
            throw new NotFoundException("Question bundle to answer not found in categorization: QuestionType: " + answer.getQuestionType() + ", ProtocolId: " + answer.getProtocolId() + ", QuestionId: " + answer.getQuestionId());
        }

        validateAndSetAnswer(questionBundle, answer);

        // Save the document
        return categorizationRepository.save(categorization);
    }

    /**
     * Routes the validation process according to the type of the question within the bundle, performing the appropriate
     * field validations and setting the answer.
     *
     * @param questionBundle the bundle containing the question and its details
     * @param answer         the answer to validate and set in the question bundle
     * @throws BadRequestException if the question type is not found
     */
    private void validateAndSetAnswer(QuestionBundle questionBundle, Answer answer) throws BadRequestException {
        switch (questionBundle.getQuestion().getQuestionType()) {
            case BASE:
                validateBaseQuestionFields((BaseQuestion) questionBundle.getQuestion(), answer);
                break;
            case PROTOCOL:
                validateProtocolQuestionFields((ProtocolQuestion) questionBundle.getQuestion(), answer);
                break;
            default:
                log.error("Unknown question type: {}", questionBundle.getQuestion().getQuestionType());
                throw new BadRequestException("Unknown question type: " + questionBundle.getQuestion().getQuestionType());
        }
        questionBundle.setAnswer(answer);
    }

    /**
     * Validates the fields of a BaseQuestion by checking field existence and the validity of the answer against the field type.
     *
     * @param question the BaseQuestion to validate
     * @param answer   the answer containing the response details
     * @throws NotFoundException if a field is not found
     */
    private void validateBaseQuestionFields(BaseQuestion question, Answer answer) throws NotFoundException {
        answer.getAnswers().keySet().forEach(fieldId -> {
            BaseQuestionField field = question.getFieldByFieldId(fieldId);
            if (field == null) {
                log.error("Field not found in base question: {}", fieldId);
                throw new NotFoundException("Field not found in base question: " + fieldId);
            }
            validateBaseField(field, answer.getAnswers().get(fieldId));
        });
    }

    /**
     * Validates the fields of a ProtocolQuestion by checking field existence and the validity of the answer against the field type.
     *
     * @param question the ProtocolQuestion to validate
     * @param answer   the answer containing the response details
     * @throws NotFoundException if a field is not found
     */
    private void validateProtocolQuestionFields(ProtocolQuestion question, Answer answer) throws NotFoundException {
        answer.getAnswers().keySet().forEach(fieldId -> {
            ProtocolQuestionField field = question.getFieldByFieldId(fieldId);
            if (field == null) {
                log.error("Field not found in protocol question: {}", fieldId);
                throw new NotFoundException("Field not found in protocol question: " + fieldId);
            }
            validateProtocolField(field, answer.getAnswers().get(fieldId));
        });
    }

    /**
     * Validates an individual field within a BaseQuestion, checking if the answer is appropriate for the field type (i.e. NUMBER,
     * SingleChoice, TEXT).
     *
     * @param field       the field within the BaseQuestion to validate
     * @param answerValue the answer value to validate against the field
     * @throws NotFoundException if the answer is not valid for the given field type or if the field type is unsupported
     * @throws ValidationException if the answer is not a valid number
     */
    private void validateBaseField(BaseQuestionField field, String answerValue) throws NotFoundException, ValidationException {
        switch (field.getType()) {
            case SINGLE_CHOICE:
                if (!field.getOptions().contains(answerValue)) {
                    log.error("Invalid answer for field: {}. Answer: {}", field.getFieldId(), answerValue);
                    throw new NotFoundException("Invalid answer for field: " + field.getFieldId() + ". Answer: " + answerValue);
                }
                break;
            case NUMBER:
                try {
                    Double.parseDouble(answerValue);
                } catch (NumberFormatException e) {
                    log.error("Invalid number format for field: {}. Answer: {}", field.getFieldId(), answerValue);
                    throw new ValidationException("Invalid number format for field: " + field.getFieldId() + ". Answer: " + answerValue);
                }
                break;
            case TEXT:
                break;
            default:
                log.error("Unsupported field type for base question: {}", field.getType());
                throw new NotFoundException("Unsupported field type for base question: " + field.getType());
        }
    }

    /**
     * Validates an individual field within a ProtocolQuestion, ensuring the answer is appropriate for the field type (i.e.,
     * SingleChoice). Throws exceptions if the answer is invalid or the field type is unsupported.
     *
     * @param field       the field within the ProtocolQuestion to validate
     * @param answerValue the answer value to validate against the field
     * @throws NotFoundException if the answer is not valid for the given field type or if the field type is unsupported
     */
    private void validateProtocolField(ProtocolQuestionField field, String answerValue) throws NotFoundException {
        if (field.getType() == FieldType.SINGLE_CHOICE) {
            if (!field.containsOptionText(answerValue)) {
                log.error("Invalid answer for field: {}. Answer: {}", field.getFieldId(), answerValue);
                throw new NotFoundException("Invalid answer for field: " + field.getFieldId() + ". Answer: " + answerValue);
            }
        } else {
            log.error("Unsupported field type for protocol question: {}", field.getType());
            throw new NotFoundException("Unsupported field type for protocol question: " + field.getType());
        }
    }

    /**
     * Returns a list of all answers for the categorization with the given categorization sessionId.
     *
     * @param sessionId the id of the categorization session
     * @return a list of all questions with answers for the given session
     * @throws NotFoundException if no categorization is found with the given sessionId
     */
    @Override
    public CategorizationDTO findById(UUID sessionId) throws NotFoundException {
        Categorization categorization = categorizationRepository.findById(sessionId).orElseThrow(
                () -> new NotFoundException("No categorization found with ID: " + sessionId)
        );

        return CategorizationMapper.toDTO(categorization);
    }

}
