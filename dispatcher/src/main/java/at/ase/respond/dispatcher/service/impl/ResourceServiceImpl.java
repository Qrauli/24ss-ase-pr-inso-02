package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.ResourceState;
import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.common.event.IncidentStatusUpdatedEvent;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.common.exception.ValidationException;
import at.ase.respond.dispatcher.persistence.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.vo.LocationCoordinatesVO;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.service.IncidentService;
import at.ase.respond.dispatcher.service.MessageSender;
import at.ase.respond.dispatcher.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    private final IncidentService incidentService;

    private final IncidentMapper incidentMapper;

    private final MessageSender messageSender;

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    @Transactional
    public Resource assignToIncident(String resourceId, UUID incidentId) {
        Incident incident = incidentService.findById(incidentId);
        Resource resource = this.findById(resourceId);

        if (resource.getState() != ResourceState.AVAILABLE) {
            throw new ValidationException("Resource is not available");
        }

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

        log.debug("Resource {} assigned to incident {}", resourceId, incidentId);

        return resource;
    }

    @Override
    @Transactional
    public Resource updateLocation(String resourceId, LocationCoordinatesVO location) throws NotFoundException {
        Resource resource = this.findById(resourceId);
        resource.setLocationCoordinates(location);
        resourceRepository.save(resource);

        log.debug("Resource {} location updated to {}", resourceId, location);

        return resource;
    }

    @Override
    @Transactional
    public Resource updateState(String resourceId, ResourceState state) {
        Resource resource = this.findById(resourceId);
        resource.setState(state);
        if (state == ResourceState.AVAILABLE && resource.getAssignedIncident() != null) {
            log.debug("Resource {} reported status AVAILABLE, unassigning from incident {}", resourceId, resource.getAssignedIncident().getId());
            resource.setAssignedIncident(null);
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

}