package at.ase.respond.incident;

import at.ase.respond.common.IncidentState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.persistence.model.LocationAddress;
import at.ase.respond.incident.persistence.model.LocationCoordinates;
import at.ase.respond.incident.presentation.controller.IncidentController;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.service.IncidentService;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class IncidentControllerTest {

    @InjectMocks
    IncidentController incidentController;

    @Mock
    IncidentService incidentService;

    @Mock
    IncidentMapper incidentMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testCreateIncident() {
        IncidentDTO incident = new IncidentDTO(
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
        when(incidentService.create(incident)).thenReturn(UUID.randomUUID());

        ResponseEntity<UUID> responseEntity = incidentController.create(incident);

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void testGetIncident() {
        UUID incidentId = UUID.randomUUID();
        IncidentDTO incident = new IncidentDTO(
            incidentId,
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
        when(incidentMapper.toDTO(incidentService.findById(incidentId))).thenReturn(incident);
        Incident serviceIncident = incidentMapper.toEntity(incident);
        when(incidentService.findById(incidentId)).thenReturn(serviceIncident);

        ResponseEntity<IncidentDTO> responseEntity = incidentController.findById(incidentId);

        assertEquals(200, responseEntity.getStatusCode().value());
    }
}