package at.ase.respond.common.exception;

/**
 * Thrown when a requested resource is not found in the system.
 *
 * <p>Note that the term <i>resource</i> is used in a broad
 * sense here and can refer to any kind of entity in the system,
 * i.e. is not limited to incident resources.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
