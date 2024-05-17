package at.ase.respond.datafeeder.service;

import java.util.List;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.ResourceDTO;
import at.ase.respond.common.exception.NotFoundException;

public interface ResourceService {

	List<ResourceDTO> findAll();

	ResourceDTO updateState(String resourceId, ResourceState newState) throws NotFoundException;

	ResourceDTO updateLocation(String resourceId, LocationCoordinatesDTO newLocation) throws NotFoundException;

}
