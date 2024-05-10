package at.ase.respond.dispatcher.presentation.event;

import at.ase.respond.dispatcher.presentation.dto.LocationCoordinatesDTO;

public record ResourceLocationUpdatedEvent(String resourceId, LocationCoordinatesDTO locationCoordinates) {
}
