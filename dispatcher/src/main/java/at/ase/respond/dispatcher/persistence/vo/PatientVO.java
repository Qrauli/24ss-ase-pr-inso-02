package at.ase.respond.dispatcher.persistence.vo;

import at.ase.respond.common.Sex;
import lombok.Data;

/**
 * Represents a patient of an incident, as a value object.
 */
@Data
public class PatientVO {

    private Integer age;

    private Sex sex;
}
