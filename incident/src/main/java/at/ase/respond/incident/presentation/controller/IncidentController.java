package at.ase.respond.incident.presentation.controller;

import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper; 
import at.ase.respond.incident.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    private final IncidentMapper incidentMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Records a new incident", security = @SecurityRequirement(name = "bearer"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<UUID> create(@RequestBody IncidentDTO payload) {
        return ResponseEntity.ok(service.create(payload));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of incidents", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<List<IncidentDTO>> findIncidents(@RequestParam(required = false) UUID[] ids) {
        if (ids == null) {
            return ResponseEntity.ok(service.findAllIncidents().stream().map(incidentMapper::toDTO).toList());
        }
        return ResponseEntity.ok(service.findIncidents(ids).stream().map(incidentMapper::toDTO).toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an incident by id", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<IncidentDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentMapper.toDTO(service.findById(id)));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a given incident", security = @SecurityRequirement(name = "bearer"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<IncidentDTO> update(@RequestBody IncidentDTO payload) {
        return ResponseEntity.ok(incidentMapper.toDTO(service.update(payload)));
    }
}
