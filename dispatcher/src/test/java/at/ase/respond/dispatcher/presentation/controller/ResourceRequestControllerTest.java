package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.common.dto.ResourceRequestDTO;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.presentation.mapper.ResourceRequestMapper;
import at.ase.respond.dispatcher.service.ResourceRequestService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResourceRequestController.class)
class ResourceRequestControllerTest {

    private static final String ENDPOINT_URL = "/requests";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResourceRequestMapper mapper;

    @MockBean
    private ResourceRequestService service;

    @Test
    void whenNoJWT_findAll_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL)
                        .param("open", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWT_findAll_then200Ok() throws Exception {
        when(service.findAll(anyBoolean())).thenReturn(List.of());

        mvc.perform(get(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenNoJWT_finishRequest_then401Unauthorized() throws Exception {
        mvc.perform(put(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000/state")
                        .with(csrf())
                        .param("open", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWTNoRequest_finishRequest_then404NotFound() throws Exception {
        when(service.finishRequest(any())).thenThrow(NotFoundException.class);

        mvc.perform(put(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000/state")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenJWT_finishRequest_then200Ok() throws Exception {
        when(service.finishRequest(any())).thenReturn(null);
        when(mapper.toDTO(any())).thenReturn(new ResourceRequestDTO(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                ResourceRequestState.FINISHED,
                ResourceType.RTW,
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "JOH-1"
        ));

        mvc.perform(put(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000/state")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("dispatcher")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"00000000-0000-0000-0000-000000000000\",\"state\":\"FINISHED\",\"requestedResourceType\":\"RTW\",\"assignedIncident\":\"00000000-0000-0000-0000-000000000000\",\"resourceId\":\"JOH-1\"}"));
    }

}