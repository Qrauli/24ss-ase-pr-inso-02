package at.ase.respond.categorization.service.impl;

import at.ase.respond.categorization.exception.NotFoundException;
import at.ase.respond.categorization.persistence.questionschema.model.Question;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionSchema;
import at.ase.respond.categorization.persistence.questionschema.model.Questions;
import at.ase.respond.categorization.service.QuestionSchemaService;
import at.ase.respond.categorization.util.QuestionSchemaLoader;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionSchemaServiceImpl implements QuestionSchemaService {

    private final QuestionSchema questionSchema;

    public QuestionSchemaServiceImpl(QuestionSchemaLoader questionSchemaLoader) {
        this.questionSchema = questionSchemaLoader.loadQuestionSchema();
    }

    @Override
    public QuestionSchema getQuestionSchema() {
        return questionSchema;
    }

    @Override
    public Question getQuestionByTypeAndId(QuestionType type, int id, Integer protocolId) {
        if (type == null) {
            throw new IllegalArgumentException("Question type must not be null");
        }

        if (type == QuestionType.BASE) {
            return questionSchema.getQuestions().getBaseQuestions().stream()
                    .filter(question -> question.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Base question with id " + id + " not found"));
        } else if (type == QuestionType.PROTOCOL) {
            if (protocolId == null) {
                throw new IllegalArgumentException("Protocol id must not be null for protocol questions");
            }

            return Optional.ofNullable(questionSchema.getQuestions())
                    .map(Questions::getProtocols)
                    .map(protocols -> Optional.ofNullable(protocols.get(protocolId))
                            .orElseThrow(() -> new NotFoundException("Protocol with id " + protocolId + " not found")))
                    .map(protocol -> Optional.ofNullable(protocol.getQuestions())
                            .orElseThrow(() -> new NotFoundException("Question list not found for protocol with id " + protocolId)))
                    .flatMap(questions -> questions.stream()
                            .filter(q -> q.getId() == id)
                            .findFirst())
                    .orElseThrow(() -> new NotFoundException("Protocol question for protocol " + protocolId + " with id " + id + " not found"));
        }

        throw new IllegalArgumentException("Unknown question type: " + type);
    }
}
