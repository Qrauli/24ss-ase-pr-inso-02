package at.ase.respond.common.dto;

import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.ResourceType;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents the meta-data of a resource request needed for dispatching.
 *
 * @param id                    the incident identifier
 * @param state                 the state of the resource request
 * @param requestedResourceType the type of resource request
 * @param assignedIncident      the incident assigned to the resource request
 * @param resourceId            the resource identifier
 * 
 */
public record ResourceRequestDTO(
        UUID id,
        ResourceRequestState state, 
        ResourceType requestedResourceType, 
        UUID assignedIncident,
        String resourceId
) implements Serializable {
}
