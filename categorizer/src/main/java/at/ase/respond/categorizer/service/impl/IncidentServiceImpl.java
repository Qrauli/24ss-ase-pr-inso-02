package at.ase.respond.categorizer.service.impl;

import at.ase.respond.categorizer.persistence.IncidentRepository;
import at.ase.respond.categorizer.persistence.model.Incident;
import at.ase.respond.categorizer.presentation.mapper.IncidentMapper;
import at.ase.respond.categorizer.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final RabbitTemplate rabbit;

    private final IncidentRepository repository;

    public UUID create() {
        Incident saved = repository.save(new Incident());

        rabbit.convertAndSend("queue", IncidentMapper.toDTO(saved));

        return saved.getId();
    }
}
