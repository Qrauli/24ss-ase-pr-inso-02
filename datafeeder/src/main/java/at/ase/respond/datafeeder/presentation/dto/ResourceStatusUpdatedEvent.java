package at.ase.respond.datafeeder.presentation.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ResourceStatusUpdatedEvent(
		OffsetDateTime timestamp,
		String resourceId,
		ResourceType type,
		ResourceState state,
		UUID assignedIncident
) {
}
