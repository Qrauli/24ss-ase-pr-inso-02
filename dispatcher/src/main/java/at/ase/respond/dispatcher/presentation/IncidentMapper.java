package at.ase.respond.dispatcher.presentation;

import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.presentation.dto.IncidentDTO;

import java.util.UUID;

public final class IncidentMapper {

    private IncidentMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static IncidentDTO toDTO(Incident incident) {
        return new IncidentDTO(incident.getId().toString());
    }

    public static Incident toEntity(IncidentDTO incident) {
        return new Incident(UUID.fromString(incident.id()));
    }

}
