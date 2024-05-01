package at.ase.respond.dispatcher.presentation.event;

/**
 * Sent or received if the status of a resource has changed.
 *
 * @param recipientResourceId the recipient of this message
 */
public record ResourceStatusUpdatedEvent(String recipientResourceId) {
}
