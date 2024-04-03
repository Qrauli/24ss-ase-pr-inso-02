package at.ase.respond.categorizer.presentation.mapper;

import at.ase.respond.categorizer.persistence.model.Incident;
import at.ase.respond.categorizer.presentation.dto.IncidentCreatedEvent;

public final class IncidentMapper {

    private IncidentMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static IncidentCreatedEvent toDTO(Incident incident) {
        return new IncidentCreatedEvent(incident.getId().toString());
    }

}
