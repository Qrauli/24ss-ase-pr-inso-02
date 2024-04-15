package at.ase.respond.categorizer.service.impl;

import at.ase.respond.categorizer.persistence.IncidentRepository;
import at.ase.respond.categorizer.persistence.model.Incident;
import at.ase.respond.categorizer.presentation.mapper.IncidentMapper;
import at.ase.respond.categorizer.service.IncidentService;
import at.ase.respond.categorizer.service.MessageSender;
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

    public UUID create() {
        Incident saved = repository.save(new Incident());
        sender.publish(IncidentMapper.toDTO(saved));
        return saved.getId();
    }

}
