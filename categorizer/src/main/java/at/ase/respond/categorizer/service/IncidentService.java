package at.ase.respond.categorizer.service;

import java.util.UUID;

public interface IncidentService {

    /**
     * Creates a new incident and publishes it to the broker.
     * @return the {@link UUID} of the newly created incident
     */
    UUID create();

}
