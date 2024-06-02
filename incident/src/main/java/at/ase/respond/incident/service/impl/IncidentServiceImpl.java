package at.ase.respond.incident.service.impl;

import at.ase.respond.incident.persistence.IncidentRepository;
import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.service.IncidentService;
import at.ase.respond.incident.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    private final MessageSender sender;

    private final IncidentMapper incidentMapper;

    @Override
    public UUID create(IncidentDTO incident) {
        log.debug("Creating incident {}", incident);
        Incident saved = repository.save(incidentMapper.toEntity(incident));
        sender.publish(incidentMapper.toEvent(saved));
        return saved.getId();
    }

    @Override
    public List<Incident> findIncidents(UUID[] ids) {
        return repository.findByIdIn(ids);
    }

    @Override
    public List<Incident> findAllIncidents() {
        return repository.findAll();
    }

    @Override
    public Incident findById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Incident update(IncidentDTO incident) {
        log.debug("Updating incident {}", incident);
        Incident saved = repository.save(incidentMapper.toEntity(incident));
        sender.publish(incidentMapper.toEvent(saved));
        return saved;
    }

}
