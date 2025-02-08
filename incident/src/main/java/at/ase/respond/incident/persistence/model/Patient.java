package at.ase.respond.incident.persistence.model;

import at.ase.respond.common.Sex;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a patient value object.
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Sex sex;

}
