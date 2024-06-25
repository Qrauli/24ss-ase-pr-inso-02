package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.logging.SignedLogger;
import at.ase.respond.dispatcher.persistence.repository.ResourceRequestRepository;
import at.ase.respond.dispatcher.persistence.model.ResourceRequest;
import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.service.ResourceRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceRequestServiceImpl implements ResourceRequestService {

    private final ResourceRequestRepository resourceRequestRepository;

    private final SignedLogger signedLogger = new SignedLogger();

    @Override
    public List<ResourceRequest> findAll(boolean openOnly) {
        log.trace("Retrieving {} resource requests", openOnly ? "open" : "all");
        return openOnly ? resourceRequestRepository.findByState(ResourceRequestState.OPEN)
                : resourceRequestRepository.findAll();
    }

    @Override
    public void create(ResourceRequest resourceRequest) {
        resourceRequestRepository.save(resourceRequest);
        signedLogger.info("Resource Request {} for incident {} and resource type {} has been created",
                resourceRequest.getId(), resourceRequest.getAssignedIncident(), resourceRequest.getRequestedResourceType());

    }

    @Override
    public ResourceRequest finishRequest(UUID id) {
        ResourceRequest updatedResourceRequest = resourceRequestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Resource request with id " + id + " not found"));
        updatedResourceRequest.setState(ResourceRequestState.FINISHED);
        resourceRequestRepository.save(updatedResourceRequest);
        signedLogger.info("Resource Request {} for incident {} and resource type {} has been completed",
                updatedResourceRequest.getId(), updatedResourceRequest.getAssignedIncident(),
                updatedResourceRequest.getRequestedResourceType());
        return updatedResourceRequest;
    }

}