package at.ase.respond.dispatcher.presentation.event;

import at.ase.respond.dispatcher.presentation.dto.IncidentDTO;

import java.time.OffsetDateTime;

public record IncidentStatusUpdatedEvent(OffsetDateTime timestamp, String resourceId, IncidentDTO assignedIncident) {
}
