package at.ase.respond.incident.service;

import at.ase.respond.incident.presentation.dto.IncidentDTO;

import java.util.UUID;

public interface IncidentService {

    /**
     * Creates a new incident and publishes it to the broker.
     *
     * @param incident the DTO containing the meta-information of the incident to be created
     * @return the {@link UUID} of the newly created incident
     */
    UUID create(IncidentDTO incident);

}
