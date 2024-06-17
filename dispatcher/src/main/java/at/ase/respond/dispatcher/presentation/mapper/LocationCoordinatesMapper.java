package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.LocationCoordinatesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Mapper(componentModel = "spring")
public interface LocationCoordinatesMapper {

    @Mapping(source = "x", target = "longitude")
    @Mapping(source = "y", target = "latitude")
    LocationCoordinatesDTO toDTO(GeoJsonPoint locationCoordinates);

    default GeoJsonPoint toGeoJsonPoint(LocationCoordinatesDTO locationCoordinates) {
        // Note that the order of the coordinates is reversed in MongoDB
        return new GeoJsonPoint(locationCoordinates.longitude(), locationCoordinates.latitude());
    }

}
