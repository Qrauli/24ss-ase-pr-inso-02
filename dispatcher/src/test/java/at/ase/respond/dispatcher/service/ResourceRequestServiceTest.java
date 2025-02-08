package at.ase.respond.dispatcher.service;

import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.ResourceRequest;
import at.ase.respond.dispatcher.persistence.repository.ResourceRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ResourceRequestServiceTest {

    @MockBean
    private ResourceRequestRepository resourceRequestRepository;

    @Autowired
    private ResourceRequestService service;

    @Test
    void whenNoRequests_findAll_thenReturnEmptyList() {
        when(resourceRequestRepository.findAll()).thenReturn(List.of());

        assertThat(service.findAll(false)).isEmpty();
    }

    @Test
    void whenNoRequests_findAllOpenOnly_thenReturnEmptyList() {
        when(resourceRequestRepository.findByState(ResourceRequestState.OPEN)).thenReturn(List.of());

        assertThat(service.findAll(true)).isEmpty();
    }

    @Test
    void save_callsRepositorySave() {
        ResourceRequest resourceRequest = new ResourceRequest();

        service.create(resourceRequest);

        verify(resourceRequestRepository).save(resourceRequest);
    }

    @Test
    void whenNoRequest_finishRequest_thenThrowNotFoundException() {
        when(resourceRequestRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.finishRequest(UUID.randomUUID()));
    }

    @Test
    void whenRequest_finishRequest_thenRequestIsFinished() {
        ResourceRequest request = new ResourceRequest();
        request.setId(UUID.randomUUID());
        request.setState(ResourceRequestState.OPEN);

        when(resourceRequestRepository.findById(any())).thenReturn(Optional.of(request));

        ResourceRequest result = service.finishRequest(request.getId());

        assertThat(result.getState()).isEqualTo(ResourceRequestState.FINISHED);
    }

}