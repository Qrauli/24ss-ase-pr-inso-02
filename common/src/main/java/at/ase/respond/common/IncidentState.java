package at.ase.respond.common;

/**
 * Represents the state of an incident.
 */
public enum IncidentState {
    /**
     * Indicates that the incident is ready to be dispatched.
     */
    READY,
    /**
     * Indicates that the incident has been dispatched.
     */
    DISPATCHED,
    /**
     * Indicates that the incident has been completed.
     */
    COMPLETED
}
