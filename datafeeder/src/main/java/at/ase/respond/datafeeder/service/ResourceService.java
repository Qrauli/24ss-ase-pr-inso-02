package at.ase.respond.datafeeder.service;

import at.ase.respond.datafeeder.presentation.dto.ResourceDTO;
import at.ase.respond.datafeeder.presentation.dto.ResourceState;
import at.ase.respond.datafeeder.presentation.dto.ResourceType;

public interface ResourceService {

	ResourceDTO create(String resourceId, ResourceType type, Double latitude, Double longitude);

	ResourceDTO updateState(String resourceId, ResourceState state);

	void updateLocation(String resourceId, Double latitude, Double longitude);

	void delete(String resourceId);

}
