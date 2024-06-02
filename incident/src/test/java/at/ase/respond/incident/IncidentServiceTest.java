package at.ase.respond.incident;

import at.ase.respond.common.IncidentState;
import at.ase.respond.incident.persistence.IncidentRepository;
import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.service.IncidentService;
import at.ase.respond.incident.service.MessageSender;
import at.ase.respond.incident.service.impl.IncidentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;

import static org.mockito.Mockito.*;

public class IncidentServiceTest {

    @InjectMocks
    IncidentServiceImpl incidentService;

    @Mock
    IncidentRepository repository;


    @Mock
    IncidentMapper incidentMapper;

    @Mock
    MessageSender sender;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        IncidentDTO incidentDTO = new IncidentDTO(
            UUID.randomUUID(),
            new ArrayList<PatientDTO>(),
            0,
            "Test",
            new LocationDTO(
                new LocationAddressDTO("","","",""),
                new LocationCoordinatesDTO(0d, 0d)
            ),
            UUID.randomUUID(),
            IncidentState.READY.toString()
        );

        Incident incident = new Incident();
        when(incidentMapper.toEntity(incidentDTO)).thenReturn(incident);
        when(repository.save(incident)).thenReturn(incident);

        incidentService.create(incidentDTO);

        verify(repository, times(1)).save(incident);
        verify(sender, times(1)).publish(null);;

    }

    @Test
    public void testFindById() {
        UUID id = UUID.randomUUID();
        incidentService.findById(id);

        verify(repository, times(1)).findById(id);
    }
}