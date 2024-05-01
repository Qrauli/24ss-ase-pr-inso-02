package at.ase.respond.dispatcher.presentation.dto;

import at.ase.respond.dispatcher.persistence.model.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.UUID;

@Schema(name = "ResourceDTO", description = "Data Transfer Object for a resource")
public record ResourceDTO(String id, ResourceType type, LocationCoordinatesDTO locationCoordinates,
        UUID assignedIncident) implements Serializable {
}