package at.ase.respond.datafeeder.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.UUID;

@Schema(name = "ResourceDTO", description = "Data Transfer Object for a resource")
public record ResourceDTO(
		@Schema(description = "The unique identifier of the resource", example = "JOH-123")
		String id,
		@Schema(description = "The type of the resource", example = "NEF")
		ResourceType type,
		@Schema(description = "The state of the resource", example = "AVAILABLE")
		ResourceState state,
		LocationCoordinatesDTO locationCoordinates,
		@Schema(description = "The unique identifier of the incident the resource is currently assigned to", example = "123e4567-e89b-12d3-a456-426614174000")
		UUID assignedIncident) implements Serializable {
}
