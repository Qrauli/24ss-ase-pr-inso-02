package at.ase.respond.categorization.service;

import at.ase.respond.categorization.exception.NotFoundException;
import at.ase.respond.categorization.persistence.questionschema.model.*;
import at.ase.respond.categorization.service.impl.QuestionSchemaServiceImpl;
import at.ase.respond.categorization.util.QuestionSchemaLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class QuestionSchemaServiceTest {

    @Mock
    private QuestionSchemaLoader questionSchemaLoader;

    @Autowired
    private QuestionSchemaService questionSchemaService;

    private QuestionSchema questionSchema;

    @BeforeEach
    void setUp() {
        questionSchema = new QuestionSchema();
        questionSchema.setVersion("1.0");
        questionSchema.setQuestions(new Questions());

        when(questionSchemaLoader.loadQuestionSchema()).thenReturn(questionSchema);

        questionSchemaService = new QuestionSchemaServiceImpl(questionSchemaLoader);
    }


    @Test
    @DisplayName("Should return question schema")
    void whenGetQuestionSchema_returnQuestionSchema() {
        QuestionSchema loadedSchema = questionSchemaService.getQuestionSchema();
        assertSame(questionSchema, loadedSchema);
    }

    @Test
    @DisplayName("Should throw NotFoundException when question was not found")
    void whenQuestionNotThere_getBaseQuestionByTypeAndId_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 1, null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when question type is null")
    void whenQuestionTypeIsNull_getQuestionByTypeAndId_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> questionSchemaService.getQuestionByTypeAndId(null, 1, null));
    }

    @Test
    @DisplayName("Should return base question by type and ID")
    void whenGetBaseQuestionByTypeAndId_returnQuestion() {
        BaseQuestion baseQuestion = new BaseQuestion();
        baseQuestion.setId(1);
        questionSchema.getQuestions().getBaseQuestions().add(baseQuestion);

        BaseQuestion loadedQuestion = (BaseQuestion) questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 1, null);
        assertSame(baseQuestion, loadedQuestion);
    }

    @Test
    @DisplayName("Should throw NotFoundException when base question not found (no base questions available)")
    void whenGetBaseQuestionByTypeAndId_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 999, null));
    }

    @Test
    @DisplayName("Should throw NotFoundException when base question not found (base questions available)")
    void whenGetBaseQuestionByTypeAndId_throwNotFoundException2() {
        BaseQuestion baseQuestion = new BaseQuestion();
        baseQuestion.setId(1);
        questionSchema.getQuestions().getBaseQuestions().add(baseQuestion);

        assertThrows(NotFoundException.class, () -> questionSchemaService.getQuestionByTypeAndId(QuestionType.BASE, 999, null));
    }

    @Test
    @DisplayName("Should return protocol question ID, and protocol ID")
    void whenGetProtocolQuestionByTypeAndId_returnQuestion() {
        ProtocolQuestion protocolQuestion = new ProtocolQuestion();
        protocolQuestion.setId(1);
        protocolQuestion.setProtocolId(1);
        questionSchema.getQuestions().getProtocols().put(1, new Protocol());
        questionSchema.getQuestions().getProtocols().get(1).getQuestions().add(protocolQuestion);

        ProtocolQuestion loadedQuestion = (ProtocolQuestion) questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 1);
        assertSame(protocolQuestion, loadedQuestion);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when protocol ID is null")
    void whenGetProtocolQuestionByTypeAndId_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, null));
    }

    @Test
    @DisplayName("Should throw NotFoundException when protocol question not found (no protocol questions available)")
    void whenGetProtocolQuestionByTypeAndId_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 1, 999));
    }

    @Test
    @DisplayName("Should throw NotFoundException when protocol question not found (protocol questions available)")
    void whenGetProtocolQuestionByTypeAndId_throwNotFoundException2() {
        ProtocolQuestion protocolQuestion = new ProtocolQuestion();
        protocolQuestion.setId(1);
        protocolQuestion.setProtocolId(1);
        questionSchema.getQuestions().getProtocols().put(1, new Protocol());
        questionSchema.getQuestions().getProtocols().get(1).getQuestions().add(protocolQuestion);

        assertThrows(NotFoundException.class, () -> questionSchemaService.getQuestionByTypeAndId(QuestionType.PROTOCOL, 999, 1));
    }
}
