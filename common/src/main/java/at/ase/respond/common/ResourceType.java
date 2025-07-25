package at.ase.respond.common;

/**
 * Represents the type of one specific resource.
 */
public enum ResourceType {
    /**
     * Indicates that the resource is a patient transport ambulance vehicle.
     */
    KTW,
    /**
     * Indicates that the resource is an emergency ambulance vehicle.
     */
    RTW,
    /**
     * Indicates that the resource is a field supervisor.
     */
    FISU,
    /**
     * Indicates that the resource is an emergency doctor's vehicle.
     */
    NEF,
    /**
     * Indicates that the resource is a helicopter.
     */
    NAH,
}
