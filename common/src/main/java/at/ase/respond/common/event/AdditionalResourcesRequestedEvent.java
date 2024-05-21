package at.ase.respond.common.event;

import at.ase.respond.common.ResourceType;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Represents an event that indicates that additional resources have been requested.
 *
 * <p>Published when additional resources have been requested by a resource.
 *
 * @param resourceId            the id of the resource that requested additional resources
 * @param requestedResourceType the type of the resource that was requested
 * @param timestamp             the timestamp of the request
 */
public record AdditionalResourcesRequestedEvent(
        String resourceId, 
        ResourceType requestedResourceType,
        ZonedDateTime timestamp
) implements Serializable {
}
