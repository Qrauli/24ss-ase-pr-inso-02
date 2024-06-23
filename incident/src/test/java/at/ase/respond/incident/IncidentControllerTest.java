package at.ase.respond.incident;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.incident.persistence.model.Location;
import at.ase.respond.incident.persistence.model.State;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.persistence.model.LocationAddress;
import at.ase.respond.incident.persistence.model.LocationCoordinates;
import at.ase.respond.incident.presentation.controller.IncidentController;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.service.IncidentService;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IncidentController.class)
public class IncidentControllerTest {

    private static final String ENDPOINT_URL = "/incidents";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IncidentService incidentService;

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
        when(incidentService.findAllIncidents()).thenReturn(List.of());

        mvc.perform(get(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("calltaker")))))
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
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("calltaker")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenJWT_findById_then200Ok() throws Exception {
        final UUID id = UUID.randomUUID();
        final IncidentDTO incidentDTO = createDummyIncidentDTO(id);
        when(incidentService.findById(eq(id))).thenReturn(null);
        when(incidentMapper.toDTO(any())).thenReturn(incidentDTO);

        final MvcResult result = mvc.perform(get(ENDPOINT_URL + "/" + id)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("calltaker")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(incidentDTO, new ObjectMapper().readValue(result.getResponse().getContentAsString(), IncidentDTO.class));
    }

    @Test
    void whenNoJWT_findIncidents_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL)
                        .param("open", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWT_findIncidents_then200Ok() throws Exception {
        final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        final List<IncidentDTO> incidentDTOList = createDummyIncidentDTOs(ids);
        final List<Incident> incidentList = createDummyIncidents(ids);
        when(incidentService.findAllIncidents()).thenReturn(incidentList);
        for (int i = 0; i < incidentList.size(); i++) {
            when(incidentMapper.toDTO(eq(incidentList.get(i)))).thenReturn(incidentDTOList.get(i));
        }

        final MvcResult result = mvc.perform(get(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("calltaker")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final List<IncidentDTO> actual = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), new TypeReference<List<IncidentDTO>>(){});
        assertEquals(incidentDTOList, actual);
    }

    @Test
    void whenNoJWT_create_then403Forbidden() throws Exception {
        IncidentDTO incidentDTO = createDummyIncidentDTO(UUID.randomUUID());
        mvc.perform(post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(incidentDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenJWT_create_then200Ok() throws Exception {
        IncidentDTO incidentDTO = createDummyIncidentDTO(UUID.randomUUID());
        UUID incidentId = UUID.randomUUID();
        when(incidentService.create(any())).thenReturn(incidentId);

        mvc.perform(post(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("calltaker")))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(incidentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string('"' + String.valueOf(incidentId) + '"'));
        // for some reason quotation marks are added to strings from content()
    }

    @Test
    void whenNoJWT_update_then403Forbidden() throws Exception {
        IncidentDTO incidentDTO = createDummyIncidentDTO(UUID.randomUUID());
        mvc.perform(put(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(incidentDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenJWT_update_then200Ok() throws Exception {
        IncidentDTO incidentDTO = createDummyIncidentDTO(UUID.randomUUID());
        when(incidentService.update(any())).thenReturn(null);
        when(incidentMapper.toDTO(any())).thenReturn(incidentDTO);

        mvc.perform(put(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("calltaker")))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(incidentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(incidentDTO)));
    }

    private List<Incident> createDummyIncidents(List<UUID> ids) {
        return ids.stream().map(this::createDummyIncident).collect(Collectors.toList());
    }

    private Incident createDummyIncident(UUID id) {
        return new Incident(
                id,
                new Location(
                        new LocationCoordinates(0d, 0d),
                        new LocationAddress("","","","")

                ),
                new ArrayList<>(),
                0,
                "",
                UUID.randomUUID(),
                State.READY,
                "Test"
        );
    }

    private List<IncidentDTO> createDummyIncidentDTOs(List<UUID> ids) {
        return ids.stream().map(this::createDummyIncidentDTO).collect(Collectors.toList());
    }

    private IncidentDTO createDummyIncidentDTO(UUID id) {
        return new IncidentDTO(
                id,
                "",
                new ArrayList<>(),
                0,
                "Test",
                new LocationDTO(
                        new LocationAddressDTO("","","",""),
                        new LocationCoordinatesDTO(0d, 0d)
                ),
                UUID.randomUUID(),
                IncidentState.READY.toString()
        );
    }
}
