package at.ase.respond.categorization.persistence.categorization.model;

import at.ase.respond.categorization.exception.BadRequestException;
import at.ase.respond.categorization.exception.InvalidQuestionTypeException;
import at.ase.respond.categorization.exception.NotFoundException;
import at.ase.respond.categorization.persistence.questionschema.model.ProtocolQuestion;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categorizations")
public class Categorization {

    @Id
    private UUID sessionId;

    private List<QuestionBundle> questionBundles = new ArrayList<>();

    private String createdBy;

    private LocalDateTime createdAt;

    private String recommendedDispatchCode;

    // Copy constructor
    public Categorization(Categorization other) {
        this.sessionId = other.sessionId;
        this.createdBy = other.createdBy;
        this.createdAt = other.createdAt;
        this.recommendedDispatchCode = other.recommendedDispatchCode;

        this.questionBundles = new ArrayList<>();
        for (QuestionBundle bundle : other.questionBundles) {
            this.questionBundles.add(new QuestionBundle(bundle));
        }
    }

    /**
     * Adds a question bundle to the list of question bundles.
     *
     * @param questionBundle the question bundle to add
     */
    public void addQuestionBundle(QuestionBundle questionBundle) {
        questionBundles.add(questionBundle);
    }

    /**
     * Returns the question bundle with the given type and id.
     *
     * @param type the type of the question
     * @param id   the id of the question
     * @return the question bundle
     */
    public QuestionBundle getQuestionBundleByTypeAndId(QuestionType type, int id, Integer protocolId) {
        for (QuestionBundle bundle : questionBundles) {
            if (bundle.getQuestion().getQuestionType().equals(type) && bundle.getQuestion().getId() == id) {
                if (type == QuestionType.BASE) {
                    return bundle;
                } else if (type == QuestionType.PROTOCOL) {
                    if (((ProtocolQuestion) bundle.getQuestion()).getProtocolId() == protocolId) {
                        return bundle;
                    }
                } else {
                    throw new InvalidQuestionTypeException("Invalid question type");
                }
            }
        }
        throw new NotFoundException("Question bundle not found");
    }

    /**
     * Checks if a question bundle with the given question type and id (and protocol id for a protocol question) exists.
     *
     * @param type the type of the question
     * @param id   the id of the question
     * @return true if the question bundle exists, false otherwise
     */
    public boolean containsQuestionBundleByTypeAndId(QuestionType type, int id, Integer protocolId) {
        for (QuestionBundle bundle : questionBundles) {
            if (bundle.getQuestion().getQuestionType().equals(type) && bundle.getQuestion().getId() == id) {
                if (type == QuestionType.BASE) {
                    return true;
                } else if (type == QuestionType.PROTOCOL) {
                    if (((ProtocolQuestion) bundle.getQuestion()).getProtocolId() == protocolId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
