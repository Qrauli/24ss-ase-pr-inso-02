package at.ase.respond.incident.presentation.controller;

import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper; 
import at.ase.respond.incident.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    private final IncidentMapper incidentMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Records a new incident")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<UUID> create(@RequestBody IncidentDTO payload) {
        return ResponseEntity.ok(service.create(payload));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of incidents")
    public ResponseEntity<List<IncidentDTO>> findIncidents(@RequestParam UUID[] ids) {
        return ResponseEntity.ok(service.findIncidents(ids).stream().map(incidentMapper::toDTO).toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an incident by id")
    public ResponseEntity<IncidentDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentMapper.toDTO(service.findById(id)));
    }

}
