package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.model.IncidentState;
import at.ase.respond.dispatcher.presentation.event.IncidentCreatedEvent;
import at.ase.respond.dispatcher.presentation.dto.IncidentDTO;

public final class IncidentMapper {

    private IncidentMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static IncidentDTO toDTO(Incident incident) {
        return new IncidentDTO(incident.getId(), incident.getState());
    }

    public static Incident toEntity(IncidentCreatedEvent incident) {
        return new Incident(incident.id(), IncidentState.READY);
    }

}
