package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.presentation.event.ResourceStatusUpdatedEvent;
import at.ase.respond.dispatcher.service.MessageSender;
import at.ase.respond.dispatcher.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final IncidentRepository incidentRepository;

    private final ResourceRepository resourceRepository;

    private final MessageSender messageSender;

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource assignToIncident(String resourceId, UUID incidentId) {
        Resource updatedResource = resourceRepository.findById(resourceId).map(resource -> {
            resource.setAssignedIncident(incidentRepository.findById(incidentId)
                .orElseThrow(() -> new NotFoundException("Incident with id " + incidentId + " not found")));
            return resourceRepository.save(resource);
        }).orElseThrow(() -> new NotFoundException("Resource with id " + resourceId + " not found"));
        log.debug("Resource {} assigned to incident {}", updatedResource.getId(), incidentId);

        // TODO: We need to think about what to send here (see GitLab discussion)
        messageSender.publish(new ResourceStatusUpdatedEvent(resourceId));
        log.debug("Resource assignment of {} to incident {} sent to exchange", updatedResource.getId(), incidentId);

        return updatedResource;
    }

}