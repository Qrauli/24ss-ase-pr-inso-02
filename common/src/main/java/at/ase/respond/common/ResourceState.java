package at.ase.respond.common;

/**
 * Represents the state of a resource.
 */
public enum ResourceState {
    /**
     * Indicates that the resource is available.
     */
    AVAILABLE,
    /**
     * Indicates that the resource has been dispatched to an incident.
     */
    DISPATCHED,
    /**
     * Indicates that the resource is on the way to an incident.
     */
    ON_ROUTE_TO_INCIDENT,
    /**
     * Indicates that the resource has arrived at the incident.
     */
    AT_INCIDENT,
    /**
     * Indicates that the resource is on the way to a hospital.
     */
    ON_ROUTE_TO_HOSPITAL,
    /**
     * Indicates that the resource has arrived at the hospital.
     */
    AT_HOSPITAL,
    /**
     * Indicates that the resource is unavailable.
     */
    UNAVAILABLE
}
