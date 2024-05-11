package at.ase.respond.categorization.persistence.categorization.model;

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

    /**
     * Adds a question bundle to the list of question bundles.
     *
     * @param questionBundle the question bundle to add
     */
    public void addQuestionBundle(QuestionBundle questionBundle) {
        questionBundles.add(questionBundle);
    }

    /**
     * Removes a question bundle from the list of question bundles.
     *
     * @param questionBundle the question bundle to remove
     */
    public void removeQuestionBundle(QuestionBundle questionBundle) {
        questionBundles.remove(questionBundle);
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
                }
            }
        }
        throw new NotFoundException("Question bundle not found");
    }
}
