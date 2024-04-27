package at.ase.respond.incident.persistence.model;

import at.ase.respond.incident.exception.OperationCodeNotFoundException;
import lombok.Getter;

/**
 * The operation code is the result of a categorization. Resources get this code to be
 * able to assess the severity of the assigned incident.
 */
@Getter
public enum OperationCode {

    STOMACHACHE("01A01"), UNKNOWN_STATE("02B01"), RESPIRATORY_ARREST("09E01");

    private final String code;

    OperationCode(String code) {
        this.code = code;
    }

    public static OperationCode from(String code) {
        for (OperationCode candidate : OperationCode.values()) {
            if (candidate.code.equals(code)) {
                return candidate;
            }
        }

        throw new OperationCodeNotFoundException("Could not determine operation code");
    }

}
