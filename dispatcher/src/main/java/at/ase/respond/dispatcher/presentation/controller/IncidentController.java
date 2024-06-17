package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.dispatcher.presentation.dto.ResourceWithDistanceDTO;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.service.IncidentService;
import at.ase.respond.dispatcher.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    private final ResourceService resourceService;

    private final IncidentMapper incidentMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of incidents returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(
            summary = "Returns a list of all incidents",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<List<IncidentDTO>> findAll(
            @RequestParam(value = "running", required = false, defaultValue = "true") boolean running
    ) {
        return ResponseEntity.ok(incidentService.findAll(running).stream().map(incidentMapper::toDTO).toList());
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
    public ResponseEntity<IncidentDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentMapper.toDTO(incidentService.findById(id)));
    }

    @GetMapping(value = "/{id}/recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendations returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Incident not found")
    })
    @Operation(
            summary = "Returns a map of resource types to recommended resources",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<List<ResourceWithDistanceDTO>> getRecommendations(@PathVariable UUID id) {
        return ResponseEntity.ok(
                resourceService.getRecommendedResources(id).stream()
                        .map(r -> new ResourceWithDistanceDTO(r.getContent().getId(), r.getDistance().getValue()))
                        .toList()
        );
    }

    @PutMapping(value = "/{id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Complete incident by id", security = @SecurityRequirement(name = "bearer"))
    @CrossOrigin
    public ResponseEntity<IncidentDTO> completeIncident(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentMapper.toDTO(incidentService.completeIncident(id)));
    }

}
