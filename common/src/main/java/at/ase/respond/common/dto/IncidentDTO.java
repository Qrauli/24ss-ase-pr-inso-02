package at.ase.respond.common.dto;

import at.ase.respond.common.IncidentState;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * Represents the meta-data of an incident needed for dispatching.
 *
 * @param id                the incident identifier
 * @param code              the operation code of the incident
 * @param state             the state of the incident
 * @param location          the location of the incident
 * @param patients          the involved patients of the incident
 * @param numberOfPatients  the number of involved patients
 * @param assignedResources the resources assigned to the incident
 * @param createdAt         the creation timestamp of the incident
 * @param updatedAt         the timestamp of the last update of the incident
 */
public record IncidentDTO(
        UUID id,
        String code,
        IncidentState state,
        LocationDTO location,
        Collection<PatientDTO> patients,
        Integer numberOfPatients,
        Collection<String> assignedResources,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) implements Serializable {
}
