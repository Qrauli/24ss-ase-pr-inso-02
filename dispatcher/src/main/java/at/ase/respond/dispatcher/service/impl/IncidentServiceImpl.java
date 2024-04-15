package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    @Override
    public List<Incident> findAll() {
        return repository.findAll();
    }

}
