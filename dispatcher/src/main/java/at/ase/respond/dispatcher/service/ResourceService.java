package at.ase.respond.dispatcher.service;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.common.exception.ValidationException;
import at.ase.respond.dispatcher.persistence.vo.LocationCoordinatesVO;
import at.ase.respond.dispatcher.persistence.model.Resource;

import java.util.List;
import java.util.UUID;

public interface ResourceService {

    Resource findById(String resourceId) throws NotFoundException;

    List<Resource> findAll();

    Resource assignToIncident(String resourceId, UUID incidentId) throws NotFoundException, ValidationException;

    Resource updateLocation(String resourceId, LocationCoordinatesVO location) throws NotFoundException;

    Resource updateState(String resourceId, ResourceState state) throws NotFoundException;

}