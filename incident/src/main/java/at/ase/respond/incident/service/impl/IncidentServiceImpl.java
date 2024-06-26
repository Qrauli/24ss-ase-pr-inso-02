package at.ase.respond.incident.service.impl;

import at.ase.respond.common.logging.LogFormatter;
import at.ase.respond.common.logging.SignedLogger;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.incident.persistence.IncidentRepository;
import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.service.IncidentService;
import at.ase.respond.incident.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    private final MessageSender sender;

    private final IncidentMapper incidentMapper;

    private final SignedLogger signedLogger = new SignedLogger();

    @Override
    public UUID create(IncidentDTO incident) {
        log.trace("Create incident {}", incident);

        Incident saved = repository.save(incidentMapper.toEntity(incident));
        sender.publish(incidentMapper.toEvent(saved));
        if (saved.getQuestionaryId() != null) {
            signedLogger.info("Created new incident {} with code {} in session {}", saved.getId(),
                    saved.getCode(), saved.getQuestionaryId());
        } else {
            signedLogger.info("Created new incident {} with code {}", saved.getId(), saved.getCode());
        }
        return saved.getId();
    }

    @Override
    public List<Incident> findIncidents(UUID[] ids) {
        log.trace("Find incidents by ids {}", Arrays.toString(ids));

        return repository.findByIdIn(ids);
    }

    @Override
    public List<Incident> findAllIncidents() {
        log.trace("Find all incidents");
        return repository.findAll();
    }

    @Override
    public Incident findById(UUID id) {
        log.trace("Find incident by id {}", id);

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident with id " + id + " not found"));
    }

    @Override
    public Incident update(IncidentDTO incident) {
        log.trace("Update incident {}", incident);
        Incident saved = repository.save(incidentMapper.toEntity(incident));
        sender.publish(incidentMapper.toEvent(saved));
        signedLogger.info("Updated incident {} at {} with code {} in session", saved.getId(), saved.getLocation(),
                saved.getCode(), saved.getQuestionaryId());
        return saved;
    }

}
