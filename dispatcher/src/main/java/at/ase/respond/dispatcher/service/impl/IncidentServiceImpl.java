package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.presentation.IncidentMapper;
import at.ase.respond.dispatcher.presentation.dto.IncidentDTO;
import at.ase.respond.dispatcher.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    @RabbitListener(queues = "queue")
    private void store(IncidentDTO event) {
        repository.save(IncidentMapper.toEntity(event));
    }

    @Override
    public List<Incident> findAll() {
        return repository.findAll();
    }
}
