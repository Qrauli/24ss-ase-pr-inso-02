package at.ase.respond.datafeeder.presentation.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdditionalResourceRequestedEvent(
        UUID correlationId,
        OffsetDateTime timestamp,
        String resourceId,
        UUID incident,
        ResourceType requestedResourceType
) {
}
