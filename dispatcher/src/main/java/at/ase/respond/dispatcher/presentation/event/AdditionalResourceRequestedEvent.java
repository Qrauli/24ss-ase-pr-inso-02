package at.ase.respond.dispatcher.presentation.event;

import at.ase.respond.dispatcher.persistence.model.ResourceType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdditionalResourceRequestedEvent(UUID correlationId, OffsetDateTime timestamp, String resourceId,
        UUID incident, ResourceType requestedResourceType) {
}
