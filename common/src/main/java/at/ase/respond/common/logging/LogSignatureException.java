package at.ase.respond.common.logging;

public class LogSignatureException extends RuntimeException {
    public LogSignatureException(String message) {
        super(message);
    }

    public LogSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

}
