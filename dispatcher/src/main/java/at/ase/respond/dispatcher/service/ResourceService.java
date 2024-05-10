package at.ase.respond.dispatcher.service;

import at.ase.respond.dispatcher.persistence.model.LocationCoordinates;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.model.ResourceState;
import at.ase.respond.dispatcher.persistence.model.ResourceType;

import java.util.List;
import java.util.UUID;

public interface ResourceService {

    List<Resource> findAll();

    Resource assignToIncident(String resourceId, UUID incidentId) throws IllegalArgumentException;

    void create(Resource resource);

    void deleteById(String resourceId);

    Resource updateLocation(String resourceId, LocationCoordinates location) throws IllegalArgumentException;

    Resource updateState(String resourceId, ResourceState state) throws IllegalArgumentException;

}