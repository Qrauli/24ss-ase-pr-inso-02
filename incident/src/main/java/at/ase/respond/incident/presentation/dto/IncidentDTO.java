package at.ase.respond.incident.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Represents an incident.
 *
 * @param id the id of the incident (can be {@code null})
 * @param patients a collection of involved patients, typically there is a single patient
 * @param numberOfPatients the number of involved patients, typically 1
 * @param categorization the categorization of the incident
 * @param location the location of the incident
 */
@Schema(description = "A DTO representing an incident")
public record IncidentDTO(UUID id, Collection<PatientDTO> patients, Integer numberOfPatients,
        CategorizationDTO categorization, LocationDTO location) implements Serializable {
}
