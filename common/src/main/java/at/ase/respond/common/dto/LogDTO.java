package at.ase.respond.common.dto;

import java.io.Serializable;

public record LogDTO(
        String message,
        String level,
        String user,
        String service,
        String timestamp,
        String signature
) implements Serializable {

}
