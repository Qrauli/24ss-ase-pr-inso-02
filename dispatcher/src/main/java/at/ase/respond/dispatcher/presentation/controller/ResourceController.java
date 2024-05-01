package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.dispatcher.presentation.mapper.ResourceMapper;
import at.ase.respond.dispatcher.presentation.dto.ResourceDTO;
import at.ase.respond.dispatcher.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of all resources")
    public ResponseEntity<List<ResourceDTO>> findAll(
            @RequestParam(value = "additional", required = false, defaultValue = "false") boolean additional) {
        return ResponseEntity.ok(service.findAll().stream().map(ResourceMapper::toDTO).toList());
    }

    @PostMapping(value = "/{resourceId}/assign/{incidentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Assigns a resource to an incident")
    public ResponseEntity<ResourceDTO> assignToIncident(@PathVariable String resourceId,
            @PathVariable UUID incidentId) {
        return ResponseEntity.ok(ResourceMapper.toDTO(service.assignToIncident(resourceId, incidentId)));
    }

}
