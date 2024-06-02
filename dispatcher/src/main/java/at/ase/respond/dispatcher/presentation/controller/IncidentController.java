package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    private final IncidentMapper incidentMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of all incidents", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<List<IncidentDTO>> findAll(
            @RequestParam(value = "running", required = false, defaultValue = "true") boolean running) {
        return ResponseEntity.ok(service.findAll(running).stream().map(incidentMapper::toDTO).toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an incident by id", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<IncidentDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentMapper.toDTO(service.findById(id)));
    }

}
