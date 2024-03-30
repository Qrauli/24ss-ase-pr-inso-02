package at.ase.respond.dispatcher.service;

import at.ase.respond.dispatcher.persistence.model.Incident;

import java.util.List;

public interface IncidentService {

    /**
     * Returns a list of all incidents.
     *
     * @return a list of all incidents
     */
    List<Incident> findAll();
}
