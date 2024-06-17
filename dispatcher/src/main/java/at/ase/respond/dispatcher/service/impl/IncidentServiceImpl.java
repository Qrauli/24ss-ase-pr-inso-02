package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.repository.IncidentRepository;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.common.IncidentState;
import at.ase.respond.common.ResourceState;
import at.ase.respond.dispatcher.service.IncidentService;
import at.ase.respond.dispatcher.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    @Lazy
    private final ResourceService resourceService;

    @Override
    public List<Incident> findAll(boolean running) {
        return running ? repository.findByStateNot(IncidentState.COMPLETED) : repository.findAll();
    }

    @Override
    public Incident findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident with id " + id + " not found"));
    }

    @Override
    public Incident save(Incident incident) {
        return repository.save(incident);
    }

    @Override
    public void unassignResource(UUID id, String resourceId) {
        Incident incident = findById(id);
        incident.getAssignedResources().remove(resourceId);
        save(incident);
    }


    @Override
    public Incident completeIncident(UUID id) {
        Incident completeIncident = findById(id);

        for(String resourceId : completeIncident.getAssignedResources()) {
            resourceService.updateState(resourceId, ResourceState.AVAILABLE);
        }

        Incident updatedIncident = findById(id);
        updatedIncident.setState(IncidentState.COMPLETED);
        save(updatedIncident);
        log.debug("Incident {} completed", updatedIncident.getId());

        return updatedIncident;
    }
    



}
