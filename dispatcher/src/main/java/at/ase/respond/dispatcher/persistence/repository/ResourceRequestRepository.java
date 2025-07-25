package at.ase.respond.dispatcher.persistence.repository;

import at.ase.respond.dispatcher.persistence.model.ResourceRequest;
import at.ase.respond.common.ResourceRequestState;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRequestRepository extends MongoRepository<ResourceRequest, UUID> {

    /**
     * Returns a list of all resource requests with the specified state.
     *
     * @param state the state of the resource requests to be returned
     * @return a list of all resource requests with the specified state
     */
    List<ResourceRequest> findByState(ResourceRequestState state);

}