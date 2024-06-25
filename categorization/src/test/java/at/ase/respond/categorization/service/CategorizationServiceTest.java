package at.ase.respond.categorization.service;

import at.ase.respond.categorization.exception.NotFoundException;
import at.ase.respond.categorization.exception.ValidationException;
import at.ase.respond.categorization.persistence.categorization.CategorizationRepository;
import at.ase.respond.categorization.persistence.categorization.model.Answer;
import at.ase.respond.categorization.persistence.categorization.model.Categorization;
import at.ase.respond.categorization.persistence.categorization.model.QuestionBundle;
import at.ase.respond.categorization.persistence.questionschema.model.*;
import at.ase.respond.categorization.presentation.dto.AnswerDTO;
import at.ase.respond.categorization.presentation.dto.CategorizationDTO;
import at.ase.respond.categorization.presentation.dto.QuestionBundleDTO;
import at.ase.respond.categorization.presentation.dto.questionschema.BaseQuestionDTO;
import at.ase.respond.categorization.presentation.dto.questionschema.FieldDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategorizationServiceTest {

    @MockBean
    private CategorizationRepository categorizationRepository;

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private QuestionSchemaService questionSchemaService;

    @Autowired
    private CategorizationService categorizationService;

    private UUID sessionId;
    private AnswerDTO answerDTO;
    private Categorization initializedCategorization;
    private Categorization answeredCategorization;
    private CategorizationDTO categorizationDTO;


    @Value("${custom.questionschema.protocolSelectorQuestionId}")
    private int protocolSelectorQuestionId;

    @BeforeEach
    void setUp() {
        sessionId = UUID.randomUUID();
        answerDTO = createDummyFirstBaseQuestionAnswerDTO();
        initializedCategorization = createDummyCategorizationWithoutAnswers(sessionId);
        answeredCategorization = createDummyCategorizationWithAnswers(sessionId);
        categorizationDTO = createDummyCategorizationDTOWithNoAnswer(sessionId);
    }

    @Test
    @DisplayName("Should create a new session successfully")
    void whenCreateSession_returnCategorizationDTO() {
        BaseQuestion baseQuestion = createDummyFirstBaseQuestion();

        when(questionSchemaService.getQuestionSchema().getQuestions().getBaseQuestions().getFirst()).thenReturn(baseQuestion);
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(initializedCategorization);

        CategorizationDTO result = categorizationService.createSession("peter.parker@werescue.org");

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isNotNull(),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isGreaterThanOrEqualTo(1);
                },
                () -> assertThat(result.createdBy()).isEqualTo(initializedCategorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo(initializedCategorization.getRecommendedDispatchCode()),
                () -> verify(categorizationRepository, times(1)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Should throw NotFoundException when session not found")
    void whenNoCategorization_save_thenThrowNotFoundException() {
        when(categorizationRepository.findById(sessionId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> categorizationService.save(sessionId, answerDTO));
    }

    @Test
    @DisplayName("Should save first BaseQuestion answer successfully")
    void whenSaveAnswer_returnAnsweredCategorizationDTO() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(initializedCategorization));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(answeredCategorization);
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 1, null)).thenReturn(createDummyFirstBaseQuestion());
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 2, null)).thenReturn(createDummyProtocolSelectorBaseQuestion());

        CategorizationDTO result = categorizationService.save(sessionId, answerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(2);
                },
                () -> assertThat(result.createdBy()).isEqualTo(answeredCategorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo(answeredCategorization.getRecommendedDispatchCode()),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Should throw NotFoundException when answer is saved with invalid fieldIds for that BaseQuestion")
    void whenInvalidBaseQuestionAnswer_saveAnswer_throwNotFoundException() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(initializedCategorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 1, null)).thenReturn(createDummyFirstBaseQuestion());

        answerDTO.answers().put("invalidFieldId", "Invalid answer");

        assertThrows(NotFoundException.class, () -> categorizationService.save(sessionId, answerDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when answer is saved with invalid selected option for a FieldType.SINGLE_CHOICE for that BaseQuestion field")
    void whenInvalidSingleChoiceBaseQuestionAnswerSelectOption_saveAnswer_throwValidationException() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(initializedCategorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 1, null)).thenReturn(createDummyFirstBaseQuestion());

        answerDTO.answers().put("gender", "cat");

        assertThrows(ValidationException.class, () -> categorizationService.save(sessionId, answerDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when answer is saved with string instead of a number for required number for that BaseQuestion field")
    void whenStringInsteadOfNumberBaseQuestionAnswer_saveAnswer_throwValidationException() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(initializedCategorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 1, null)).thenReturn(createDummyFirstBaseQuestion());

        answerDTO.answers().put("numberOfPeople", "Not a number");

        assertThrows(ValidationException.class, () -> categorizationService.save(sessionId, answerDTO));
    }

    @Test
    @DisplayName("Should save ProtocolQuestion answer successfully and return CategorizationDTO with next ProtocolQuestion")
    void whenSaveAnswer_returnAnsweredCategorizationDTOWithNextProtocolQuestion() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId)));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(answeredCategorization);
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 2, 1)).thenReturn(createDummyProtocolQuestion(2, 1));

        AnswerDTO answer = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Nein"));
        CategorizationDTO result = categorizationService.save(sessionId, answer);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(2);
                },
                () -> assertThat(result.createdBy()).isEqualTo(answeredCategorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo(answeredCategorization.getRecommendedDispatchCode()),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Should throw NotFoundException when answer is saved with invalid fieldIds for that ProtocolQuestion")
    void whenInvalidProtocolQuestionAnswer_saveAnswer_throwNotFoundException() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(createDummyCategorizationWithProtocolQuestionWithoutAnswer(UUID.randomUUID())));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("invalidFieldId", "Invalid answer"));

        assertThrows(NotFoundException.class, () -> categorizationService.save(sessionId, newAnswerDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when answerDTO is saved with invalid select option for a FieldType.SINGLE_CHOICE for that ProtocolQuestion field")
    void whenInvalidSingleChoiceProtocolQuestionAnswerSelectOption_saveAnswer_throwValidationException() {
        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(createDummyCategorizationWithProtocolQuestionWithoutAnswer(UUID.randomUUID())));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Invalid option"));

        assertThrows(ValidationException.class, () -> categorizationService.save(sessionId, newAnswerDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when answerDTO is saved with a question that has not FieldType.SINGLE_CHOICE for that ProtocolQuestion field")
    void whenNotSingleChoiceProtocolQuestionAnswer_saveAnswer_throwValidationException() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(UUID.randomUUID());
        ((ProtocolQuestion) categorization.getQuestionBundles().get(2).getQuestion()).getFields().getFirst().setType(FieldType.NUMBER);

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Ja"));

        assertThrows(ValidationException.class, () -> categorizationService.save(sessionId, newAnswerDTO));
    }

    @Test
    @DisplayName("Should unset recommendedDispatchCode when answer for protocolSelectorQuestionId is edited and the dispatchCode was already set")
    void whenEditAnswerForProtocolSelectorQuestionIdWithDifferentAnswer_saveAnswer_unsetRecommendedDispatchCodeIfAlreadySet() {
        Categorization categorization = createDummyCategorizationWithProtocolSelectorQuestionWithoutAnswer(sessionId);
        categorization.setRecommendedDispatchCode("01D01");
        categorization.getQuestionBundles().get(1).setAnswer(new Answer(QuestionType.BASE, 2, 0, Map.of("mpdsProtocolId", "2-Allergie/Kontakt mit giftigen Tieren")));

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 2, null)).thenReturn(createDummyProtocolSelectorBaseQuestion());
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 2));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.BASE, 2, 0, Map.of("mpdsProtocolId", "1-Bauchschmerzen/-beschwerden"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(3);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertNull(result.dispatchCode()),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Should return the first protocol Question after answering the protocolSelectorQuestion")
    void whenAnswerProtocolSelectorQuestion_saveAnswer_returnFirstCategorizationProtocolQuestion() {
        Categorization categorization = createDummyCategorizationWithProtocolSelectorQuestionWithoutAnswer(sessionId);

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 2, null)).thenReturn(createDummyProtocolSelectorBaseQuestion());
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);


        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.BASE, 2, 0, Map.of("mpdsProtocolId", "1-Bauchschmerzen/-beschwerden"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(3);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isNull(),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Should not change anything and not insert the next question because it is already inside")
    void whenEditAnswerProtocolSelectorQuestionWithSameAnswer_saveAnswer_returnSameObject() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 2, null)).thenReturn(createDummyProtocolSelectorBaseQuestion());
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.BASE, 2, 0, Map.of("mpdsProtocolId", "1-Bauchschmerzen/-beschwerden"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(3);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isNull(),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Handle ProtocolQuestion answer that leads to a DispatchCode where this one was not already answered previously")
    void whenAnswerProtocolQuestionToDispatchCode_saveAnswer_returnAnsweredCategorizationDTOWithDispatchCode() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);
        categorization.getQuestionBundles().get(2).setAnswer(createDummyProtocolQuestionAnswerToDispatchCode());

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Ja"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(3);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo("01D01"),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Handle ProtocolQuestion answer that leads to a DispatchCode where this one was already answered previously, but remains the same")
    void whenAnswerProtocolQuestionToDispatchCodeWithSameDispatchCode_saveAnswer_returnSameObject() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);
        categorization.setRecommendedDispatchCode("01D01");
        categorization.getQuestionBundles().get(2).setAnswer(createDummyProtocolQuestionAnswerToDispatchCode());

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Ja"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(3);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo("01D01"),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Handle ProtocolQuestion answer that leads to a DispatchCode where this one was already answered previously, but changes")
    void whenAnswerProtocolQuestionToDispatchCodeWithDifferentDispatchCode_saveAnswer_returnAnsweredCategorizationDTOWithNewDispatchCode() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);
        categorization.setRecommendedDispatchCode("01D01");
        categorization.getQuestionBundles().get(2).setAnswer(createDummyProtocolQuestionAnswerToDispatchCode());

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 2, 1)).thenReturn(createDummyProtocolQuestion(2, 1));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Nein"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(4);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isNull(),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Handle ProtocolQuestion answer that leads to the next question where the next question is already in the QuestionBundle list")
    void whenAnswerProtocolQuestionToNextQuestionAlreadyInQuestionBundleList_saveAnswer_returnSameObject() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);
        categorization.getQuestionBundles().get(2).setAnswer(createDummyProtocolQuestionAnswerToNextQuestion());
        categorization.getQuestionBundles().add(new QuestionBundle(createDummyProtocolQuestion(2, 1), null));

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 2, 1)).thenReturn(createDummyProtocolQuestion(2, 1));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Nein"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(4);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isNull(),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Handle dispatchCodeQuestion where the the question was edited. Previously it lead to the next question, now it should leave to a dispatchCode")
    void whenEditDispatchCodeQuestionToDispatchCode_saveAnswer_returnAnsweredCategorizationDTOWithDispatchCode() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);
        categorization.getQuestionBundles().get(2).setAnswer(createDummyProtocolQuestionAnswerToNextQuestion());
        categorization.getQuestionBundles().add(new QuestionBundle(createDummyProtocolQuestion(2, 1), null));

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(createDummyProtocolQuestion(1, 1));

        Categorization categorizationAfter = new Categorization(categorization);
        categorizationAfter.getQuestionBundles().get(2).setAnswer(createDummyProtocolQuestionAnswerToDispatchCode());

        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorizationAfter);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Ja"));

        CategorizationDTO result = categorizationService.save(sessionId, newAnswerDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isEqualTo(3);
                },
                () -> assertThat(result.createdBy()).isEqualTo(categorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo("01D01"),
                () -> verify(categorizationRepository, times(2)).save(any(Categorization.class))
        );
    }

    @Test
    @DisplayName("Throw NotFoundException if it does not find the next protocol question in the handler for the handleNextProtocolQuestion")
    void whenAnswerProtocolQuestionToNextQuestionNotInQuestionBundleList_saveAnswer_throwNotFoundException() {
        Categorization categorization = createDummyCategorizationWithProtocolQuestionWithoutAnswer(sessionId);

        ProtocolQuestion question = createDummyProtocolQuestion(1, 1);
        question.getFields().getLast().getOptions().getLast().setNextProtocolQuestion(null);

        when(categorizationRepository.findById(sessionId)).thenReturn(Optional.of(categorization));
        when(categorizationRepository.save(any(Categorization.class))).thenReturn(categorization);
        when(questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1)).thenReturn(question);

        AnswerDTO newAnswerDTO = new AnswerDTO(QuestionType.PROTOCOL, 1, 1, Map.of("consciousness", "Nein"));

        assertThrows(NotFoundException.class, () -> categorizationService.save(sessionId, newAnswerDTO));
    }

    @Test
    @DisplayName("Should return CategorizationDTO successfully")
    void whenFindById_returnCategorizationDTO() {
        when(categorizationRepository.findById(any(UUID.class))).thenReturn(Optional.of(answeredCategorization));

        CategorizationDTO result = categorizationService.findById(sessionId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.sessionID()).isEqualTo(sessionId),
                () -> {
                    assert result.questionBundles() != null;
                    assertThat(result.questionBundles().size()).isGreaterThanOrEqualTo(1);
                },
                () -> assertThat(result.createdBy()).isEqualTo(answeredCategorization.getCreatedBy()),
                () -> assertThat(result.createdAt()).isNotNull(),
                () -> assertThat(result.dispatchCode()).isEqualTo(answeredCategorization.getRecommendedDispatchCode()),
                () -> verify(categorizationRepository, times(1)).findById(sessionId)
        );
    }

    @Test
    @DisplayName("Should throw NotFoundException when session not found")
    void whenNonExistingSessionId_findById_throwNotFoundException() {
        when(categorizationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(NotFoundException.class, () -> categorizationService.findById(sessionId)),
                () -> verify(categorizationRepository, times(1)).findById(sessionId)
        );
    }


    private BaseQuestion createDummyFirstBaseQuestion() {
        BaseQuestion baseQuestion = new BaseQuestion();
        baseQuestion.setId(1);
        baseQuestion.setQuestionType(QuestionType.BASE);
        baseQuestion.setText("Wo ist der Unfallort?");
        baseQuestion.setNextBaseQuestionId(2);

        final List<BaseQuestionField> fields = List.of(
                new BaseQuestionField("city", "Stadt", FieldType.TEXT, List.of()),
                new BaseQuestionField("street", "Straße", FieldType.TEXT, List.of()),
                new BaseQuestionField("postalCode", "PostalCode", FieldType.TEXT, List.of()),
                new BaseQuestionField("lat", "Latitude", FieldType.TEXT, List.of()),
                new BaseQuestionField("lon", "Longitude", FieldType.TEXT, List.of()),
                new BaseQuestionField("additionalData", "Zusatzinformationen", FieldType.TEXT, List.of()),
                new BaseQuestionField("name", "Name", FieldType.TEXT, List.of()),
                new BaseQuestionField("phone", "Telefonnummer", FieldType.TEXT, List.of()),
                new BaseQuestionField("numberOfPeople", "Anzahl", FieldType.NUMBER, List.of()),
                new BaseQuestionField("age", "Alter", FieldType.NUMBER, List.of()),
                new BaseQuestionField("gender", "Geschlecht", FieldType.SINGLE_CHOICE, List.of("Male", "Female", "Unknown"))
        );

        baseQuestion.setFields(fields);
        return baseQuestion;
    }

    private BaseQuestion createDummyProtocolSelectorBaseQuestion() {
        BaseQuestion baseQuestion = new BaseQuestion();
        baseQuestion.setId(2);
        baseQuestion.setQuestionType(QuestionType.BASE);
        baseQuestion.setText("Was ist passiert?");

        BaseQuestionField field = new BaseQuestionField();
        field.setFieldId("mpdsProtocolId");
        field.setText("Auswahl");
        field.setType(FieldType.SINGLE_CHOICE);
        field.setOptions(Arrays.asList(
                "1-Bauchschmerzen/-beschwerden",
                "2-Allergie/Kontakt mit giftigen Tieren",
                "3-Tierbiss/-angriff",
                "4-Verbrechen",
                "5-Rückenschmerzen",
                "6-Atembeschwerden",
                "7-Verbrennungen/Explosion",
                "8-Kohlenmonoxid",
                "9-Kreislaufstillstand",
                "10-Brustschmerzen",
                "11-Ersticken",
                "12-Krampfanfall",
                "13-Blutzuckerentgleisung",
                "14-Ertrinken",
                "15-Stromunfall/Blitzschlag",
                "16-Augenprobleme/-verletzungen",
                "17-Sturz/Absturz",
                "18-Kopfschmerzen",
                "19-Herzbeschwerden",
                "20-Hitze-/Kälteprobleme",
                "21-Blutung",
                "22-Unzugängliche/Verschüttete Person",
                "23-Überdosis/Vergiftung",
                "24-Schwangerschaft",
                "25-Psychiatrie",
                "26-Kranke Person",
                "27-Stich-/Schuss-/Pfählungsverletzung",
                "28 Schlaganfall",
                "29-Verkehrsunfall",
                "30-Verletzungen",
                "31-Bewusstlosigkeit/Ohnmacht (Beinahe-)",
                "32-Unklares Geschehen",
                "33-Transportanforderung",
                "37-Untersuchung/Transport (von/zu Versorgungseinrichtung)",
                "42-Nachforderung"
        ));

        baseQuestion.setFields(List.of(field));

        return baseQuestion;
    }

    private ProtocolQuestion createDummyProtocolQuestion(int id, int protocolId) {
        ProtocolQuestion protocolQuestion = new ProtocolQuestion();
        protocolQuestion.setId(id);
        protocolQuestion.setProtocolId(protocolId);
        protocolQuestion.setQuestionType(QuestionType.PROTOCOL);
        protocolQuestion.setText("Liegt eine Bewusstseinstrübung vor?");

        ProtocolQuestionOption option1 = new ProtocolQuestionOption();
        option1.setText("Ja");
        option1.setDispatchCode("01D01");

        ProtocolQuestionOption option2 = new ProtocolQuestionOption();
        option2.setText("Nein");
        option2.setNextProtocolQuestion(new NextProtocolQuestion(1, 2));

        final List<ProtocolQuestionOption> options = List.of(option1, option2);
        final List<ProtocolQuestionField> fields = List.of(new ProtocolQuestionField("consciousness", FieldType.SINGLE_CHOICE, options));

        protocolQuestion.setFields(fields);

        return protocolQuestion;
    }

    private Answer createDummyFirstBaseQuestionAnswer() {
        Answer answer = new Answer();
        answer.setQuestionType(QuestionType.BASE);
        answer.setQuestionId(1);
        answer.setProtocolId(0);
        Map<String, String> answers = new HashMap<>();
        answers.put("city", "Vienna");
        answers.put("street", "Karlsplatz 4");
        answers.put("postalCode", "1010");
        answers.put("lat", "48.1995");
        answers.put("lon", "16.3699");
        answers.put("additionalData", "Additional information");
        answers.put("name", "John Doe");
        answers.put("phone", "123456789");
        answers.put("numberOfPeople", "2");
        answers.put("age", "30");
        answers.put("gender", "Male");
        answer.setAnswers(answers);
        return answer;
    }

    private Answer createDummyProtocolQuestionAnswerToDispatchCode() {
        Answer answer = new Answer();
        answer.setQuestionType(QuestionType.PROTOCOL);
        answer.setQuestionId(1);
        answer.setProtocolId(1);
        Map<String, String> answers = new HashMap<>();
        answers.put("consciousness", "Ja");
        answer.setAnswers(answers);
        return answer;
    }

    private Answer createDummyProtocolQuestionAnswerToNextQuestion() {
        Answer answer = new Answer();
        answer.setQuestionType(QuestionType.PROTOCOL);
        answer.setQuestionId(1);
        answer.setProtocolId(1);
        Map<String, String> answers = new HashMap<>();
        answers.put("consciousness", "Nein");
        answer.setAnswers(answers);
        return answer;
    }

    private Categorization createDummyCategorizationWithoutAnswers(UUID sessionId) {
        BaseQuestion baseQuestion = createDummyFirstBaseQuestion();

        QuestionBundle questionBundle = new QuestionBundle();
        questionBundle.setQuestion(baseQuestion);

        Categorization categorization = new Categorization();
        categorization.setSessionId(sessionId);
        categorization.setCreatedBy("peter.parker@werescue.org");
        categorization.setCreatedAt(LocalDateTime.now());
        categorization.setRecommendedDispatchCode(null);
        categorization.addQuestionBundle(questionBundle);

        return categorization;
    }

    private Categorization createDummyCategorizationWithProtocolSelectorQuestionWithoutAnswer(UUID sessionId) {
        BaseQuestion baseQuestion = createDummyFirstBaseQuestion();

        QuestionBundle questionBundle1 = new QuestionBundle();
        questionBundle1.setQuestion(baseQuestion);
        questionBundle1.setAnswer(createDummyFirstBaseQuestionAnswer());

        QuestionBundle questionBundle2 = new QuestionBundle();
        questionBundle2.setQuestion(createDummyProtocolSelectorBaseQuestion());

        Categorization categorization = new Categorization();
        categorization.setSessionId(sessionId);
        categorization.setCreatedBy("peter.parker@werescue.org");
        categorization.setCreatedAt(LocalDateTime.now());
        categorization.setRecommendedDispatchCode(null);
        categorization.addQuestionBundle(questionBundle1);
        categorization.addQuestionBundle(questionBundle2);
        return categorization;
    }

    private Categorization createDummyCategorizationWithProtocolQuestionWithoutAnswer(UUID sessionId) {
        BaseQuestion baseQuestion = createDummyFirstBaseQuestion();

        QuestionBundle questionBundle1 = new QuestionBundle();
        questionBundle1.setQuestion(baseQuestion);
        questionBundle1.setAnswer(createDummyFirstBaseQuestionAnswer());

        QuestionBundle questionBundle2 = new QuestionBundle();
        questionBundle2.setQuestion(createDummyProtocolSelectorBaseQuestion());
        questionBundle2.setAnswer(new Answer(QuestionType.BASE, 2, 0, Map.of("mpdsProtocolId", "1-Bauchschmerzen/-beschwerden")));

        QuestionBundle questionBundle3 = new QuestionBundle();
        questionBundle3.setQuestion(createDummyProtocolQuestion(1, 1));

        Categorization categorization = new Categorization();
        categorization.setSessionId(sessionId);
        categorization.setCreatedBy("peter.parker@werescue.org");
        categorization.setCreatedAt(LocalDateTime.now());
        categorization.setRecommendedDispatchCode(null);
        categorization.addQuestionBundle(questionBundle1);
        categorization.addQuestionBundle(questionBundle2);
        categorization.addQuestionBundle(questionBundle3);
        return categorization;
    }

    private Categorization createDummyCategorizationWithAnswers(UUID sessionId) {
        BaseQuestion baseQuestion = createDummyFirstBaseQuestion();

        QuestionBundle questionBundle = new QuestionBundle();
        questionBundle.setQuestion(baseQuestion);
        questionBundle.setAnswer(createDummyFirstBaseQuestionAnswer());

        Categorization categorization = new Categorization();
        categorization.setSessionId(sessionId);
        categorization.setCreatedBy("peter.parker@werescue.org");
        categorization.setCreatedAt(LocalDateTime.now());
        categorization.setRecommendedDispatchCode(null);
        categorization.addQuestionBundle(questionBundle);

        return categorization;
    }


    private CategorizationDTO createDummyCategorizationDTOWithNoAnswer(UUID sessionId) {
        final List<FieldDTO> fields = List.of(
                new FieldDTO("city", "Stadt", FieldType.TEXT, List.of()),
                new FieldDTO("street", "Straße", FieldType.TEXT, List.of()),
                new FieldDTO("postalCode", "PostalCode", FieldType.TEXT, List.of()),
                new FieldDTO("lat", "Latitude", FieldType.TEXT, List.of()),
                new FieldDTO("lon", "Longitude", FieldType.TEXT, List.of()),
                new FieldDTO("additionalData", "Zusatzinformationen", FieldType.TEXT, List.of()),
                new FieldDTO("name", "Name", FieldType.TEXT, List.of()),
                new FieldDTO("phone", "Telefonnummer", FieldType.TEXT, List.of()),
                new FieldDTO("numberOfPeople", "Anzahl", FieldType.NUMBER, List.of()),
                new FieldDTO("age", "Alter", FieldType.NUMBER, List.of()),
                new FieldDTO("gender", "Geschlecht", FieldType.SINGLE_CHOICE, List.of("Male", "Female", "Unknown"))
        );

        final BaseQuestionDTO baseQuestion = new BaseQuestionDTO(QuestionType.BASE, 1, "Wo ist der Unfallort?", fields);
        final QuestionBundleDTO questionBundle = new QuestionBundleDTO(baseQuestion, null, null);

        return new CategorizationDTO(sessionId, List.of(questionBundle), "peter.parker@werescue.org", LocalDateTime.now().toString(), null);
    }

    private CategorizationDTO createDummyCategorizationDTOWithAnswers(UUID sessionId) {
        final List<FieldDTO> fields = List.of(
                new FieldDTO("city", "Stadt", FieldType.TEXT, List.of()),
                new FieldDTO("street", "Straße", FieldType.TEXT, List.of()),
                new FieldDTO("postalCode", "PostalCode", FieldType.TEXT, List.of()),
                new FieldDTO("lat", "Latitude", FieldType.TEXT, List.of()),
                new FieldDTO("lon", "Longitude", FieldType.TEXT, List.of()),
                new FieldDTO("additionalData", "Zusatzinformationen", FieldType.TEXT, List.of()),
                new FieldDTO("name", "Name", FieldType.TEXT, List.of()),
                new FieldDTO("phone", "Telefonnummer", FieldType.TEXT, List.of()),
                new FieldDTO("numberOfPeople", "Anzahl", FieldType.NUMBER, List.of()),
                new FieldDTO("age", "Alter", FieldType.NUMBER, List.of()),
                new FieldDTO("gender", "Geschlecht", FieldType.SINGLE_CHOICE, List.of("Male", "Female", "Unknown"))
        );

        final BaseQuestionDTO baseQuestion = new BaseQuestionDTO(QuestionType.BASE, 1, "Wo ist der Unfallort?", fields);
        final QuestionBundleDTO questionBundle = new QuestionBundleDTO(baseQuestion, null, createDummyFirstBaseQuestionAnswerDTO());

        return new CategorizationDTO(sessionId, List.of(questionBundle), "peter.parker@werescue.org", LocalDateTime.now().toString(), null);
    }

    private AnswerDTO createDummyFirstBaseQuestionAnswerDTO() {
        Map<String, String> answers = new HashMap<>();
        answers.put("city", "Vienna");
        answers.put("street", "Karlsplatz 4");
        answers.put("postalCode", "1010");
        answers.put("lat", "48.1995");
        answers.put("lon", "16.3699");
        answers.put("additionalData", "Additional information");
        answers.put("name", "John Doe");
        answers.put("phone", "123456789");
        answers.put("numberOfPeople", "2");
        answers.put("age", "30");
        answers.put("gender", "Male");

        return new AnswerDTO(QuestionType.BASE, 1, 0, answers);
    }

    private AnswerDTO createDummyProtocolQuestionAnswerToDispatchCodeDTO() {
        Map<String, String> answers = new HashMap<>();
        answers.put("consciousness", "Ja");

        return new AnswerDTO(QuestionType.PROTOCOL, 1, 1, answers);
    }
}