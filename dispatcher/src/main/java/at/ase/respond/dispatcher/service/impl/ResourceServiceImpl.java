package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.common.event.IncidentStatusUpdatedEvent;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.common.logging.SignedLogger;
import at.ase.respond.dispatcher.persistence.repository.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.service.IncidentService;
import at.ase.respond.dispatcher.service.MessageSender;
import at.ase.respond.dispatcher.service.ResourceService;
import at.ase.respond.dispatcher.service.ResponseRegulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResponseRegulationService responseRegulationService;

    private final ResourceRepository resourceRepository;

    private final IncidentService incidentService;

    private final IncidentMapper incidentMapper;

    private final MessageSender messageSender;

    private final SignedLogger signedLogger = new SignedLogger();

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 2))
    public Resource assignToIncident(String resourceId, UUID incidentId) {
        Incident incident = incidentService.findById(incidentId);
        Resource resource = this.findById(resourceId);

        resource.setState(ResourceState.DISPATCHED);
        resource.setAssignedIncident(incident);
        resourceRepository.save(resource);

        incident.setState(IncidentState.DISPATCHED);
        incident.getAssignedResources().add(resourceId);
        incidentService.save(incident);

        // Notify all other assigned resources about the new resource
        ZonedDateTime timestamp = ZonedDateTime.now();
        IncidentDTO incidentDTO = incidentMapper.toDTO(incident);
        for (String assignedResourceId : incident.getAssignedResources()) {
            IncidentStatusUpdatedEvent event = new IncidentStatusUpdatedEvent(assignedResourceId, incidentDTO, timestamp);
            messageSender.publish(event);
        }

        signedLogger.info("Dispatched resource {} to incident {}", resourceId, incidentId);

        return resource;
    }

    @Override
    @Transactional
    public Resource updateLocation(String resourceId, GeoJsonPoint location) throws NotFoundException {
        Resource resource = this.findById(resourceId);
        resource.setLocationCoordinates(location);
        resourceRepository.save(resource);

        log.trace("Resource {} location updated to {}", resourceId, location);

        return resource;
    }

    @Override
    @Transactional
    public Resource updateState(String resourceId, ResourceState state) {
        Resource resource = this.findById(resourceId);
        resource.setState(state);
        if (state == ResourceState.AVAILABLE && resource.getAssignedIncident() != null) {
            UUID incidentId = resource.getAssignedIncident().getId();
            log.debug("Resource {} reported status AVAILABLE, unassigning from incident {}", resourceId, incidentId);
            resource.setAssignedIncident(null);
            incidentService.unassignResource(incidentId, resourceId);
        }
        resourceRepository.save(resource);

        log.debug("Resource {} state updated to {}", resourceId, state);

        return resource;
    }

    @Override
    public Resource findById(String resourceId) {
        return resourceRepository.findById(resourceId)
                .orElseThrow(() -> new NotFoundException("Resource with id " + resourceId + " not found"));
    }

    @Override
    @Transactional
    public List<GeoResult<Resource>> getRecommendedResources(UUID id) throws NotFoundException {
        log.trace("Finding recommended resources for incident {}", id);

        Incident incident = incidentService.findById(id);
        GeoJsonPoint incidentLocation = incident.getLocation().getCoordinates();

        // Get the required resource types (possibly more than one)
        List<ResourceType> resourceTypes = responseRegulationService.getRecommendedResourceTypes(incident.getCode());
        log.debug("Recommended resource types for incident {}: {}", id, resourceTypes);

        List<GeoResult<Resource>> recommended = resourceTypes.stream()
                .flatMap(resourceType -> resourceRepository.findRecommendedResources(incidentLocation, resourceType).stream())
                .toList();
        signedLogger.info("Recommended resources for incident {}: {}", id, recommended.stream()
                .map(GeoResult::getContent).collect(Collectors.toList()));
        return recommended;
    }

}