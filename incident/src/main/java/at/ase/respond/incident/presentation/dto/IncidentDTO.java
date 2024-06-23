package at.ase.respond.incident.presentation.dto;

import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an incident.
 *
 * @param id the id of the incident (can be {@code null})
 * @param patients a collection of involved patients, typically there is a single patient
 * @param numberOfPatients the number of involved patients, typically 1
 * @param code the operation code of the incident
 * @param location the location of the incident
 * @param questionaryId the id of the questionary associated with the incident
 */
@Schema(description = "A DTO representing an incident")
public record IncidentDTO(UUID id, Collection<PatientDTO> patients, Integer numberOfPatients,
            String code, LocationDTO location, UUID questionaryId, String state) implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentDTO that = (IncidentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(state, that.state) && Objects.equals(questionaryId, that.questionaryId) && Objects.equals(numberOfPatients, that.numberOfPatients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfPatients, code, questionaryId, state);
    }
}
