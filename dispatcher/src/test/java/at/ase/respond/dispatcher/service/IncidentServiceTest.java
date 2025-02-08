package at.ase.respond.dispatcher.service;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class IncidentServiceTest {

    @MockBean
    private IncidentRepository incidentRepository;

    @MockBean
    private ResourceService resourceService;

    @Autowired
    private IncidentService service;

    @Test
    void whenNoIncidents_findAllOnlyRunning_thenReturnEmptyList() {
        when(incidentRepository.findByStateNot(IncidentState.COMPLETED)).thenReturn(List.of());

        assertThat(service.findAll(true)).isEmpty();
    }

    @Test
    void whenNoIncidents_findAllNotOnlyRunning_thenReturnEmptyList() {
        when(incidentRepository.findAll()).thenReturn(List.of());

        assertThat(service.findAll(false)).isEmpty();
    }

    @Test
    void whenNoIncident_findById_thenThrowNotFoundException() {
        when(incidentRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.findById(UUID.randomUUID()));
    }

    @Test
    void whenIncidentExists_findById_thenReturnIncident() {
        Incident incidentToFind = new Incident();
        incidentToFind.setId(UUID.randomUUID());

        when(incidentRepository.findById(any())).thenReturn(Optional.of(incidentToFind));

        Incident result = service.findById(incidentToFind.getId());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(incidentToFind.getId())
        );
    }

    @Test
    void whenSaveIncident_thenReturnSavedIncident() {
        Incident incidentToSave = new Incident();
        incidentToSave.setId(UUID.randomUUID());

        when(incidentRepository.save(any())).thenReturn(incidentToSave);

        Incident result = service.save(incidentToSave);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(incidentToSave.getId())
        );
    }

    @Test
    void whenUnassignResource_thenRemoveResourceFromIncident() {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.getAssignedResources().add("JOK-1");
        incident.getAssignedResources().add("JOK-2");

        when(incidentRepository.findById(any())).thenReturn(Optional.of(incident));

        service.unassignResource(incident.getId(), "JOK-2");

        assertThat(incident.getAssignedResources()).doesNotContain("JOK-2");
    }

    @Test
    void whenCompleteIncident_thenUpdateIncidentAndResources() {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.getAssignedResources().add("JOK-1");
        incident.getAssignedResources().add("JOK-2");

        when(incidentRepository.findById(any())).thenReturn(Optional.of(incident));

        service.completeIncident(incident.getId());

        assertThat(incident.getState()).isEqualTo(IncidentState.COMPLETED);
    }

}