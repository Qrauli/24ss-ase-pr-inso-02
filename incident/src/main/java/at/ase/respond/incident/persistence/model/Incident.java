package at.ase.respond.incident.persistence.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents the incident entity.
 *
 * <p>
 * Note that the location and patient value object is embedded in the entity, since we do
 * not model locations and patients as reference objects.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OperationCode code;

    @Embedded
    private Location location;

    @ElementCollection
    private Collection<Patient> patients;

    private Integer numberOfPatients;

    private UUID questionaryId;

}
