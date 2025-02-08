package at.ase.respond.common.event;

import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * Represents the meta-data of an incident needed for dispatching.
 *
 * <p>Published when a new incident is created or an existing incident is updated.
 *
 * @param id               the incident identifier
 * @param code             the operation code of the incident
 * @param callerNumber     the phone number of the caller
 * @param location         the location of the incident
 * @param patients         the involved patients of the incident
 * @param numberOfPatients the number of involved patients
 * @param timestamp        the creation or update timestamp of the incident
 */
public record IncidentCreatedOrUpdatedEvent(
        UUID id,
        String code,
        String callerNumber,
        LocationDTO location,
        Collection<PatientDTO> patients,
        Integer numberOfPatients,
        ZonedDateTime timestamp
) implements Serializable {
}
