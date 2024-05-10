package at.ase.respond.datafeeder.presentation.dto;

import java.time.OffsetDateTime;

public record IncidentStatusUpdatedEvent(
		OffsetDateTime timestamp,
		String resourceId,
		IncidentDTO assignedIncident
) {
}
