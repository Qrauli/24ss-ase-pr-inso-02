package at.ase.respond.datafeeder.presentation.dto;

public record ResourceLocationUpdatedEvent(String resourceId, LocationCoordinatesDTO locationCoordinates) {
}
