package at.ase.respond.dispatcher.service;

import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.ResourceRequest;

import java.util.List;
import java.util.UUID;

public interface ResourceRequestService {


    /**
     * Returns a list of all resource requests. If {@code openOnly} is true,
     * only open requests are returned.
     *
     * @param openOnly if true, only open requests are returned
     * @return a list of all resource requests
     */
    List<ResourceRequest> findAll(boolean openOnly);

    /**
     * Creates a new resource request.
     *
     * @param resource the resource request to be created
     */
    void create(ResourceRequest resource);

    /**
     * Finishes the resource request with the specified id.
     *
     * @param id the id of the resource request to be finished
     * @return the finished resource request
     * @throws NotFoundException if the resource request with the specified id is not present
     */
    ResourceRequest finishRequest(UUID id) throws NotFoundException;

}