package at.ase.respond.categorizer.presentation.dto;

import at.ase.respond.categorizer.persistence.model.Sex;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents a patient.
 *
 * @param age the approximate age of the patient
 * @param sex the sex of the patient
 */
@Schema(description = "A DTO representing a single patient")
public record PatientDTO(Integer age, Sex sex) implements Serializable {
}
