package at.ase.respond.incident.presentation.dto;

import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Represents an incident.
 *
 * @param id the id of the incident (can be {@code null})
 * @param callerNumber the phone number of the caller
 * @param patients a collection of involved patients, typically there is a single patient
 * @param numberOfPatients the number of involved patients, typically 1
 * @param code the operation code of the incident
 * @param location the location of the incident
 * @param questionaryId the id of the questionary associated with the incident
 */
@Schema(description = "A DTO representing an incident")
public record IncidentDTO(
        UUID id,
        String callerNumber,
        Collection<PatientDTO> patients,
        Integer numberOfPatients,
        String code,
        LocationDTO location,
        UUID questionaryId,
        String state
) implements Serializable {
}
