package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.common.dto.ResourceDTO;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.common.exception.ValidationException;
import at.ase.respond.dispatcher.presentation.mapper.ResourceMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResourceController.class)
class ResourceControllerTest {

    private static final String ENDPOINT_URL = "/resources";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResourceMapper mapper;

    @MockBean
    private ResourceService service;

    @Test
    void whenNoJWT_findAll_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL)
                        .param("additional", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWT_findAll_then200Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mvc.perform(get(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .param("additional", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenNoJWT_assignToIncident_then401Unauthorized() throws Exception {
        mvc.perform(post(ENDPOINT_URL + "/1/assign/00000000-0000-0000-0000-000000000000")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWTNoIncident_assignToIncident_then404NotFound() throws Exception {
        when(service.assignToIncident(any(), any())).thenThrow(NotFoundException.class);

        mvc.perform(post(ENDPOINT_URL + "/1/assign/00000000-0000-0000-0000-000000000000")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenJWTResourceNotAvailable_assignToIncident_then400BadRequest() throws Exception {
        when(service.assignToIncident(any(), any())).thenThrow(ValidationException.class);

        mvc.perform(post(ENDPOINT_URL + "/1/assign/00000000-0000-0000-0000-000000000000")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenJWT_assignToIncident_then200Ok() throws Exception {
        when(service.assignToIncident(any(), any())).thenReturn(null);
        when(mapper.toDTO(any())).thenReturn(new ResourceDTO(
                "JOH-1",
                ResourceType.RTW,
                ResourceState.DISPATCHED,
                null,
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                null
        ));

        mvc.perform(post(ENDPOINT_URL + "/1/assign/00000000-0000-0000-0000-000000000000")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"JOH-1\",\"type\":\"RTW\",\"state\":\"DISPATCHED\",\"locationCoordinates\":null,\"assignedIncident\":\"00000000-0000-0000-0000-000000000000\",\"updatedAt\":null}"));
    }

}