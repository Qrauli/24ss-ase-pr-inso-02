package at.ase.respond.incident.presentation.controller;

import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper; 
import at.ase.respond.incident.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
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
    public ResponseEntity<UUID> create(@RequestBody IncidentDTO payload, Principal principal) {
        MDC.put("user", principal.getName());
        return ResponseEntity.ok(service.create(payload));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of incidents", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<List<IncidentDTO>> findIncidents(@RequestParam(required = false) UUID[] ids, Principal principal) {
        MDC.put("user", principal.getName());
        return ResponseEntity.ok(service.findAllIncidents().stream().map(incidentMapper::toDTO).toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incident returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Incident not found")
    })
    @Operation(
            summary = "Returns an incident by id",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<IncidentDTO> findById(@PathVariable UUID id, Principal principal) {
        MDC.put("user", principal.getName());
        return ResponseEntity.ok(incidentMapper.toDTO(service.findById(id)));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a given incident", security = @SecurityRequirement(name = "bearer"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<IncidentDTO> update(@RequestBody IncidentDTO payload, Principal principal) {
        MDC.put("user", principal.getName());
        return ResponseEntity.ok(incidentMapper.toDTO(service.update(payload)));
    }
}
