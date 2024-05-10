package at.ase.respond.datafeeder.presentation.dto;

import java.io.Serializable;
import java.util.UUID;

public record IncidentDTO(UUID id, IncidentState state) implements Serializable {
}
