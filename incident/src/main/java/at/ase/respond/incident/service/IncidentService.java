package at.ase.respond.incident.service;

import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.presentation.dto.IncidentDTO;

import java.util.List;
import java.util.UUID;

public interface IncidentService {

    /**
     * Creates a new incident and publishes it to the broker.
     *
     * @param incident the DTO containing the meta-information of the incident to be created
     * @return the {@link UUID} of the newly created incident
     */
    UUID create(IncidentDTO incident);

    /**
     * Finds incidents by their IDs.
     * @param ids the IDs of the incidents to be found
     * @return a list of incidents
     */
    List<Incident> findIncidents(UUID[] ids);

    /**
     * Finds all incidents.
     * @return a list of incidents
     */
    List<Incident> findAllIncidents();

    /**
     * Finds an incident by its ID.
     * @param id the ID of the incident to be found
     * @return the incident
     */
    Incident findById(UUID id);

    /**
     * Updates a given incident.
     *
     * @param incident the incident to be updated
     * @return the updates incident
     */
    Incident update(IncidentDTO incident);

}
