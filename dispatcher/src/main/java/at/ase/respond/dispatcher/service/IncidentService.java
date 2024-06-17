package at.ase.respond.dispatcher.service;

import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.Incident;

import java.util.List;
import java.util.UUID;

public interface IncidentService {

    /**
     * Returns a list of all incidents.
     *
     * @param running if true, only non-completed incidents are returned
     * @return a list of all incidents
     */
    List<Incident> findAll(boolean running);

    /**
     * Returns the incident with the specified id, if present.
     *
     * @param id the id of the incident to return
     * @return the incident with the specified id, if present
     * @throws NotFoundException if the incident with the specified id is not present
     */
    Incident findById(UUID id) throws NotFoundException;

    /**
     * Saves the specified incident and returns the saved incident.
     *
     * @param incident the incident to save
     * @return the saved incident
     */
    Incident save(Incident incident);

    /**
     * Unassigns the specified resource from the incident with the specified id.
     *
     * @param id         the id of the incident
     * @param resourceId the id of the resource to unassign
     */
    void unassignResource(UUID id, String resourceId);

}
