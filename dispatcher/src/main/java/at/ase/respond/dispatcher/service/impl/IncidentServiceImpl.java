package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.common.IncidentState;
import at.ase.respond.dispatcher.service.IncidentService;
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

}
