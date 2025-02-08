package at.ase.respond.categorization.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Categorization.
 *
 * @param sessionID the session id of the categorization
 * @param questionBundles the question bundles of the categorization
 * @param createdBy the user who created the categorization
 * @param createdAt the date when the categorization was created
 */
@Schema(description = "A DTO representing a categorization")
public record CategorizationDTO(UUID sessionID, List<QuestionBundleDTO> questionBundles, String createdBy, String createdAt,
                                String dispatchCode) implements Serializable {

}
