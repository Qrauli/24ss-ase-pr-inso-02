package at.ase.respond.categorization.presentation.mapper;

import at.ase.respond.categorization.persistence.categorization.model.Categorization;
import at.ase.respond.categorization.presentation.dto.CategorizationDTO;

import java.util.stream.Collectors;

public class CategorizationMapper {

    private CategorizationMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static CategorizationDTO toDTO(Categorization categorization) {
        if (categorization == null) {
            return null;
        }

        return new CategorizationDTO(categorization.getSessionId(),
                categorization.getQuestionBundles().stream()
                        .map(QuestionBundleMapper::toDTO).collect(Collectors.toList()),
                categorization.getCreatedBy(),
                categorization.getCreatedAt().toString(),
                categorization.getRecommendedDispatchCode());
    }

}
