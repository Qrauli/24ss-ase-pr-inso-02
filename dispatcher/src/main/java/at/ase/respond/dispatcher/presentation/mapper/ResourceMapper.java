package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.dispatcher.persistence.model.*;
import at.ase.respond.dispatcher.presentation.dto.LocationCoordinatesDTO;
import at.ase.respond.dispatcher.presentation.dto.ResourceDTO;

import java.util.List;

public final class ResourceMapper {

    private ResourceMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static ResourceDTO toDTO(Resource resource) {
        Double lat = resource.getLocationCoordinates().getLatitude();
        Double lon = resource.getLocationCoordinates().getLongitude();
        ResourceState state = resource.getState();
        LocationCoordinatesDTO locationCoordinatesDTO = new LocationCoordinatesDTO(lat, lon);
        Incident assignedIncident = resource.getAssignedIncident();
        return switch (assignedIncident) {
            case null -> new ResourceDTO(resource.getId(), resource.getType(), state, locationCoordinatesDTO, null);
            case Incident incident ->
                new ResourceDTO(resource.getId(), resource.getType(), state, locationCoordinatesDTO, incident.getId());
        };
    }

    public static Resource toEntity(ResourceDTO resourceDTO) {
        return new Resource(resourceDTO.id(), resourceDTO.type(), resourceDTO.state(),
                new LocationCoordinates(resourceDTO.locationCoordinates().latitude(),
                        resourceDTO.locationCoordinates().longitude()),
                new Incident(resourceDTO.assignedIncident(), IncidentState.READY));
    }

    public static List<ResourceDTO> toDTOs(List<Resource> resources) {
        return resources.stream().map(ResourceMapper::toDTO).toList();
    }

}