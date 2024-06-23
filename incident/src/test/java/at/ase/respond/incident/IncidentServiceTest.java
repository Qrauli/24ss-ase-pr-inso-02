package at.ase.respond.incident;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.incident.persistence.IncidentRepository;
import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.service.MessageSender;
import at.ase.respond.incident.service.impl.IncidentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository repository;

    @Mock
    private MessageSender sender;

    @Mock
    private IncidentMapper incidentMapper;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    private Incident incident;
    private IncidentDTO incidentDTO;
    private UUID incidentId;
    private IncidentCreatedOrUpdatedEvent incidentEvent;

    @BeforeEach
    void setUp() {
        incidentId = UUID.randomUUID();
        incident = new Incident();
        incident.setId(incidentId);
        final LocationDTO locationDTO = new LocationDTO(
                new LocationAddressDTO("", "", "", ""),
                new LocationCoordinatesDTO(0d, 0d)
        );
        incidentDTO = new IncidentDTO(
                incidentId,
                new ArrayList<PatientDTO>(),
                0,
                "Test",
                locationDTO,
                UUID.randomUUID(),
                IncidentState.READY.toString()
        );
        incidentEvent = new IncidentCreatedOrUpdatedEvent(
                incidentId,
                "Test",
                locationDTO,
                new ArrayList<PatientDTO>(),
                0,
                ZonedDateTime.now()
        );
    }

    @Test
    void testCreate() {
        when(incidentMapper.toEntity(incidentDTO)).thenReturn(incident);
        when(repository.save(incident)).thenReturn(incident);
        when(incidentMapper.toEvent(incident)).thenReturn(incidentEvent);

        UUID result = incidentService.create(incidentDTO);

        assertEquals(incidentId, result);
        verify(repository).save(incident);
        verify(sender).publish(incidentEvent);
    }

    @Test
    void testFindIncidents() {
        UUID[] ids = {incidentId};
        List<Incident> incidents = Collections.singletonList(incident);
        when(repository.findByIdIn(ids)).thenReturn(incidents);

        List<Incident> result = incidentService.findIncidents(ids);

        assertEquals(incidents, result);
        verify(repository).findByIdIn(ids);
    }

    @Test
    void testFindAllIncidents() {
        List<Incident> incidents = Collections.singletonList(incident);
        when(repository.findAll()).thenReturn(incidents);

        List<Incident> result = incidentService.findAllIncidents();

        assertEquals(incidents, result);
        verify(repository).findAll();
    }

    @Test
    void testFindById_Found() {
        when(repository.findById(incidentId)).thenReturn(Optional.of(incident));

        Incident result = incidentService.findById(incidentId);

        assertEquals(incident, result);
        verify(repository).findById(incidentId);
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(incidentId)).thenReturn(Optional.empty());

        Incident result = incidentService.findById(incidentId);

        assertNull(result);
        verify(repository).findById(incidentId);
    }

    @Test
    void testUpdate() {
        when(incidentMapper.toEntity(incidentDTO)).thenReturn(incident);
        when(repository.save(incident)).thenReturn(incident);
        when(incidentMapper.toEvent(incident)).thenReturn(incidentEvent);

        Incident result = incidentService.update(incidentDTO);

        assertEquals(incident, result);
        verify(repository).save(incident);
        verify(sender).publish(incidentEvent);
    }
}
