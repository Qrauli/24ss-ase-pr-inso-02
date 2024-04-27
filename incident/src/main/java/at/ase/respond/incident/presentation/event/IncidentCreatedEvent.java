package at.ase.respond.incident.presentation.event;

import at.ase.respond.incident.presentation.dto.LocationDTO;
import at.ase.respond.incident.presentation.dto.PatientDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Represents the meta-data of an incident needed for dispatching. Published when a new
 * incident was created.
 *
 * @param id the id of the incident
 * @param code the operation code of the incident
 * @param location the location of the incident
 * @param patients the involved patients of the incident
 * @param numberOfPatients the number of involved patients
 */
public record IncidentCreatedEvent(UUID id, String code, LocationDTO location, Collection<PatientDTO> patients,
        Integer numberOfPatients) implements Serializable {
}
