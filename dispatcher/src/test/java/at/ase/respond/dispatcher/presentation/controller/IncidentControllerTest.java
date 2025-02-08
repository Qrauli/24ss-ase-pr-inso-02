package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.service.IncidentService;
import at.ase.respond.dispatcher.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IncidentController.class)
class IncidentControllerTest {

    private static final String ENDPOINT_URL = "/incidents";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IncidentService incidentService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private IncidentMapper incidentMapper;

    @Test
    void whenNoJWT_findAll_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL)
                        .param("open", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWT_findAll_then200Ok() throws Exception {
        when(incidentService.findAll(anyBoolean())).thenReturn(List.of());

        mvc.perform(get(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenNoJWT_findById_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000")
                        .param("open", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWTNoIncident_findById_then404NotFound() throws Exception {
        when(incidentService.findById(any())).thenThrow(NotFoundException.class);

        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenJWT_findById_then200Ok() throws Exception {
        when(incidentService.findById(any())).thenReturn(null);
        when(incidentMapper.toDTO(any())).thenReturn(new IncidentDTO(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "01A01",
                "1234567890",
                IncidentState.READY,
                null,
                List.of(),
                1,
                List.of(),
                null,
                null
        ));

        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"00000000-0000-0000-0000-000000000000\",\"callerNumber\":\"1234567890\",\"code\":\"01A01\",\"state\":\"READY\",\"location\":null,\"patients\":[],\"numberOfPatients\":1,\"assignedResources\":[],\"createdAt\":null,\"updatedAt\":null}"));
    }

    @Test
    void whenNoJWT_getRecommendations_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000/recommendations")
                        .param("open", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWTNoIncident_getRecommendations_then404NotFound() throws Exception {
        when(resourceService.getRecommendedResources(any())).thenThrow(NotFoundException.class);

        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000/recommendations")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenJWT_getRecommendations_then200Ok() throws Exception {
        when(resourceService.getRecommendedResources(any())).thenReturn(List.of());

        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000/recommendations")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

}