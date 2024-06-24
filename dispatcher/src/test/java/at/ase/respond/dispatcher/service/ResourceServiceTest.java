package at.ase.respond.dispatcher.service;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.repository.ResourceRepository;
import at.ase.respond.dispatcher.persistence.vo.LocationVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ResourceServiceTest {

    @MockBean
    private ResponseRegulationService responseRegulationService;

    @MockBean
    private ResourceRepository resourceRepository;

    @MockBean
    private IncidentService incidentService;

    @MockBean
    private MessageSender messageSender;

    @Autowired
    private ResourceService service;

    @Test
    void whenNoResources_findAll_thenReturnEmptyList() {
        when(resourceRepository.findAll()).thenReturn(List.of());

        assertThat(service.findAll()).isEmpty();
    }

    @Test
    void whenNoIncident_assignToIncident_thenThrowNotFoundException() {
        when(incidentService.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.assignToIncident("JOK-1", UUID.randomUUID()));
    }

    @Test
    void whenNoResource_assignToIncident_thenThrowNotFoundException() {
        when(incidentService.findById(any())).thenReturn(new Incident());
        when(resourceRepository.findById("JOK-1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.assignToIncident("JOK-1", UUID.randomUUID()));
    }

    @Test
    void whenResourcesAndIncident_assignToIncident_thenReturnAssignedResourceAndAssignedResourcesAreNotified() {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.getAssignedResources().add("JOK-1");
        incident.getAssignedResources().add("JOK-2");

        Resource resource = new Resource();
        resource.setId("JOK-3");

        when(incidentService.findById(incident.getId())).thenReturn(incident);
        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        Resource result = service.assignToIncident(resource.getId(), incident.getId());

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(resource.getId()),
                () -> assertThat(result.getState()).isEqualTo(ResourceState.DISPATCHED),
                () -> assertThat(result.getAssignedIncident().getId()).isEqualTo(incident.getId()),
                () -> assertThat(incident.getAssignedResources()).containsExactly("JOK-1", "JOK-2", "JOK-3")
        );

        // All assigned resources are notified
        verify(messageSender, times(incident.getAssignedResources().toArray().length)).publish(any());
    }

    @Test
    void whenNoResource_updateLocation_thenThrowNotFoundException() {
        when(resourceRepository.findById("JOK-2")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateLocation("JOK-2", new GeoJsonPoint(0, 0)));
    }

    @Test
    void whenResourceExists_updateLocation_thenReturnUpdatedResource() {
        Resource resource = new Resource();
        resource.setId("JOK-1");
        resource.setLocationCoordinates(new GeoJsonPoint(0, 0));

        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        GeoJsonPoint newLocation = new GeoJsonPoint(10, 10);
        Resource result = service.updateLocation("JOK-1", newLocation);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(resource.getId()),
                () -> assertThat(result.getLocationCoordinates()).isEqualTo(newLocation)
        );
    }

    @Test
    void whenNoResource_updateState_thenThrowNotFoundException() {
        when(resourceRepository.findById("JOK-2")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateState("JOK-2", ResourceState.AT_HOSPITAL));
    }

    @Test
    void whenResourceExistsAndStateChangeToAtHospital_updateState_thenReturnUpdatedResource() {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());

        Resource resource = new Resource();
        resource.setId("JOK-1");
        resource.setAssignedIncident(incident);

        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        Resource result = service.updateState("JOK-1", ResourceState.AT_HOSPITAL);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(resource.getId()),
                () -> assertThat(result.getState()).isEqualTo(ResourceState.AT_HOSPITAL),
                () -> assertThat(result.getAssignedIncident().getId()).isEqualTo(incident.getId())
        );
    }

    @Test
    void whenResourceExistsAndStateChangeToAvailable_updateState_thenReturnUpdatedResource() {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());

        Resource resource = new Resource();
        resource.setId("JOK-1");
        resource.setAssignedIncident(incident);

        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        Resource result = service.updateState("JOK-1", ResourceState.AVAILABLE);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(resource.getId()),
                () -> assertThat(result.getState()).isEqualTo(ResourceState.AVAILABLE),
                () -> assertThat(result.getAssignedIncident()).isNull()
        );

        verify(incidentService).unassignResource(incident.getId(), resource.getId());
    }

    @Test
    void whenNoResource_findById_thenThrowNotFoundException() {
        when(resourceRepository.findById("JOK-2")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById("JOK-2"));
    }

    @Test
    void whenResourceExists_findById_thenReturnResource() {
        Resource resource = new Resource();
        resource.setId("JOK-1");

        when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        assertThat(service.findById("JOK-1").getId()).isEqualTo(resource.getId());
    }

    @Test
    void whenNoIncident_getRecommendedResources_thenThrowNotFoundException() {
        when(incidentService.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getRecommendedResources(UUID.randomUUID()));
    }

    @Test
    void whenNoResources_getRecommendedResources_thenReturnEmptyList() {
        LocationVO location = new LocationVO();
        location.setCoordinates(new GeoJsonPoint(0, 0));

        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setLocation(location);
        incident.setCode("01A01");

        when(incidentService.findById(incident.getId())).thenReturn(incident);
        when(responseRegulationService.getRecommendedResourceTypes(incident.getCode())).thenReturn(List.of());

        assertThat(service.getRecommendedResources(incident.getId())).isEmpty();
    }

    @Test
    void whenResources_getRecommendedResources_thenReturnRecommendedResources() {
        LocationVO location = new LocationVO();
        location.setCoordinates(new GeoJsonPoint(0, 0));

        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setLocation(location);
        incident.setCode("01A01");

        Resource rtw = new Resource();
        GeoResult<Resource> rtwGeoResult = new GeoResult<>(rtw, new Distance(10));

        Resource nef = new Resource();
        GeoResult<Resource> nefGeoResult = new GeoResult<>(nef, new Distance(20));

        when(incidentService.findById(incident.getId())).thenReturn(incident);
        when(responseRegulationService.getRecommendedResourceTypes(incident.getCode())).thenReturn(List.of(ResourceType.RTW, ResourceType.NEF));
        when(resourceRepository.findRecommendedResources(incident.getLocation().getCoordinates(), ResourceType.RTW)).thenReturn(List.of(rtwGeoResult));
        when(resourceRepository.findRecommendedResources(incident.getLocation().getCoordinates(), ResourceType.NEF)).thenReturn(List.of(nefGeoResult));

        assertThat(service.getRecommendedResources(incident.getId())).containsExactly(rtwGeoResult, nefGeoResult);
    }

}