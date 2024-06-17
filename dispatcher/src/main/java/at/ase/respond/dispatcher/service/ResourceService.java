package at.ase.respond.dispatcher.service;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.common.exception.ValidationException;
import at.ase.respond.dispatcher.persistence.model.Resource;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.UUID;

public interface ResourceService {

    /**
     * Returns the resource with the specified id, if present.
     *
     * @param resourceId the id of the resource to be found
     * @return the resource with the specified id, if present
     * @throws NotFoundException if the resource with the specified id is not present
     */
    Resource findById(String resourceId) throws NotFoundException;

    /**
     * Returns a list of all resources.
     *
     * @return a list of all resources
     */
    List<Resource> findAll();

    /**
     * Assigns the resource with the specified id to the incident with the specified id.
     *
     * @param resourceId the id of the resource to be assigned
     * @param incidentId the id of the incident to which the resource is to be assigned
     * @return the assigned resource
     * @throws NotFoundException if the resource or incident with the specified id is not present
     * @throws ValidationException if the resource is not available
     */
    Resource assignToIncident(String resourceId, UUID incidentId) throws NotFoundException, ValidationException;

    /**
     * Updates the location of the resource with the specified id.
     *
     * @param resourceId the id of the resource whose location is to be updated
     * @param location the new location of the resource
     * @return the updated resource
     * @throws NotFoundException if the resource with the specified id is not present
     */
    Resource updateLocation(String resourceId, GeoJsonPoint location) throws NotFoundException;

    /**
     * Updates the state of the resource with the specified id.
     *
     * @param resourceId the id of the resource whose state is to be updated
     * @param state the new state of the resource
     * @return the updated resource
     * @throws NotFoundException if the resource with the specified id is not present
     */
    Resource updateState(String resourceId, ResourceState state) throws NotFoundException;

    /**
     * Returns an ordered list of the top 10 recommended resources for the incident with the specified id.
     * Note that these resources are grouped by type and ordered by distance. Also, those resources are
     * either available or dispatched.
     *
     * @param id the id of the incident
     * @return an ordered list of recommended resources for the incident with the specified id
     * @throws NotFoundException if the incident with the specified id is not present
     */
    List<GeoResult<Resource>> getRecommendedResources(UUID id) throws NotFoundException;

}