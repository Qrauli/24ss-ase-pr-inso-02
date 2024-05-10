package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.LocationCoordinates;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.model.ResourceState;
import at.ase.respond.dispatcher.presentation.event.ResourceStatusUpdatedEvent;
import at.ase.respond.dispatcher.service.MessageSender;
import at.ase.respond.dispatcher.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static at.ase.respond.dispatcher.persistence.model.ResourceState.DISPATCHED;

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
            resource.setState(DISPATCHED);
            resource.setAssignedIncident(incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident with id " + incidentId + " not found")));
            return resourceRepository.save(resource);
        }).orElseThrow(() -> new IllegalArgumentException("Resource with id " + resourceId + " not found"));
        log.debug("Resource {} assigned to incident {}", updatedResource.getId(), incidentId);

        ResourceStatusUpdatedEvent event = new ResourceStatusUpdatedEvent(OffsetDateTime.now(), updatedResource.getId(),
                updatedResource.getType(), updatedResource.getState(), incidentId);
        messageSender.publish(event);
        log.debug("Resource assignment of {} to incident {} sent to exchange", updatedResource.getId(), incidentId);

        return updatedResource;
    }

    @Override
    public void create(Resource resource) {
        resourceRepository.save(resource);
    }

    @Override
    public void deleteById(String resourceId) {
        resourceRepository.deleteById(resourceId);
    }

    @Override
    public Resource updateLocation(String resourceId, LocationCoordinates location) throws IllegalArgumentException {
        Resource updatedResource = resourceRepository.findById(resourceId).map(resource -> {
            resource.setLocationCoordinates(location);
            return resourceRepository.save(resource);
        }).orElseThrow(() -> new IllegalArgumentException("Resource with id " + resourceId + " not found"));
        log.debug("Resource {} location updated to {}", updatedResource.getId(), location);
        return updatedResource;
    }

    @Override
    public Resource updateState(String resourceId, ResourceState state) {
        Resource updatedResource = resourceRepository.findById(resourceId).map(resource -> {
            resource.setState(state);
            return resourceRepository.save(resource);
        }).orElseThrow(() -> new IllegalArgumentException("Resource with id " + resourceId + " not found"));
        log.debug("Resource {} state updated to {}", updatedResource.getId(), state);

        return updatedResource;
    }

}