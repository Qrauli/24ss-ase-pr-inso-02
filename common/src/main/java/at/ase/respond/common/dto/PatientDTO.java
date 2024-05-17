package at.ase.respond.common.dto;

import at.ase.respond.common.Sex;

import java.io.Serializable;

/**
 * Represents a single patient.
 *
 * @param age the approximate age of the patient
 * @param sex the biological sex of the patient
 */
public record PatientDTO(
        Integer age,
        Sex sex
) implements Serializable {
}
