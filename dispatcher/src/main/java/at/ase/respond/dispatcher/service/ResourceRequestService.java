package at.ase.respond.dispatcher.service;

import at.ase.respond.dispatcher.persistence.model.ResourceRequest;

import java.util.List;
import java.util.UUID;

public interface ResourceRequestService {


    /**
     * run a query to find all resource requests
     * @param openOnly if true, only open requests are returned
     * @return a list of all resource requests
     */
    List<ResourceRequest> findAll(boolean openOnly);

    /**
     * run a query to find a resource request by id
     * @param id the id of the resource request to return
     * @return the resource request with the specified id, if present
     */
    void create(ResourceRequest resource);

    /**
     * run a query to finish a resource request
     * @param id the id of the resource request to finish
     * @return the finished resource request
     */
    ResourceRequest finishRequest(UUID id);

}