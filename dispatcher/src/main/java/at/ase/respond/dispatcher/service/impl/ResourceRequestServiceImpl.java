package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.ResourceRepository;
import at.ase.respond.dispatcher.persistence.ResourceRequestRepository;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.model.ResourceRequest;
import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.ResourceState;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.service.MessageSender;
import at.ase.respond.dispatcher.service.ResourceRequestService;
import at.ase.respond.dispatcher.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceRequestServiceImpl implements ResourceRequestService {

    private final ResourceRequestRepository resourceRequestRepository;

    @Override
    public List<ResourceRequest> findAll(boolean openOnly) {
        return openOnly ? resourceRequestRepository.findByState(ResourceRequestState.OPEN)
                : resourceRequestRepository.findAll();
    }

    @Override
    public void create(ResourceRequest resourceRequest) {
        resourceRequestRepository.save(resourceRequest);
    }

    @Override
    public ResourceRequest finishRequest(UUID id) {
        ResourceRequest updatedResourceRequest = resourceRequestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Resource with id " + id + " not found"));
        updatedResourceRequest.setState(ResourceRequestState.FINISHED);
        resourceRequestRepository.save(updatedResourceRequest);
        log.debug("Resource Request {} finished", updatedResourceRequest.getId());
        return updatedResourceRequest;
    }

}