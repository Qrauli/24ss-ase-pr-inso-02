package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.dispatcher.persistence.model.LocationCoordinates;
import at.ase.respond.dispatcher.presentation.dto.LocationCoordinatesDTO;

public final class LocationCoordinatesMapper {

    private LocationCoordinatesMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static LocationCoordinatesDTO toDTO(LocationCoordinates locationCoordinates) {
        return new LocationCoordinatesDTO(locationCoordinates.getLatitude(), locationCoordinates.getLongitude());
    }

    public static LocationCoordinates toEntity(LocationCoordinatesDTO locationCoordinates) {
        return new LocationCoordinates(locationCoordinates.latitude(), locationCoordinates.longitude());
    }

}
