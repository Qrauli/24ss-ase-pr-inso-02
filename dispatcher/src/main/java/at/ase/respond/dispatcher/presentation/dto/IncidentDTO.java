package at.ase.respond.dispatcher.presentation.dto;

import at.ase.respond.dispatcher.persistence.model.IncidentState;

import java.io.Serializable;
import java.util.UUID;

public record IncidentDTO(UUID id, IncidentState state) implements Serializable {
}
