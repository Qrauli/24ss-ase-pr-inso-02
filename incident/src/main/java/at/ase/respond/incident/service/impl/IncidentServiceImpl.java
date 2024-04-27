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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    private final MessageSender sender;

    public UUID create(IncidentDTO incident) {
        Incident saved = repository.save(IncidentMapper.toEntity(incident));
        sender.publish(IncidentMapper.toEvent(saved));
        return saved.getId();
    }

}
