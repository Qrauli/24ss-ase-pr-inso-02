package at.ase.respond.datafeeder.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.ResourceDTO;
import at.ase.respond.datafeeder.service.ResourceService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all resources")
    public ResponseEntity<Iterable<ResourceDTO>> getResources() {
        return ResponseEntity.ok(resourceService.findAll());
    }

    @PutMapping(value = "/{resourceId}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates the state of a resource")
    public ResponseEntity<ResourceDTO> updateResourceState(
            @PathVariable String resourceId,
            @RequestBody ResourceState state
    ) {
        return ResponseEntity.ok(resourceService.updateState(resourceId, state));
    }

    @PutMapping(value = "/{resourceId}/location", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates the location of a resource")
    public ResponseEntity<ResourceDTO> updateResourceLocation(
            @PathVariable String resourceId,
            @RequestBody LocationCoordinatesDTO location
    ) {
        return ResponseEntity.ok(resourceService.updateLocation(resourceId, location));
    }

    @PutMapping(value = "/{resourceId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Moves a resource to a new location")
    public ResponseEntity<Void> moveResource(
            @PathVariable String resourceId,
            @RequestBody LocationCoordinatesDTO location,
            @RequestParam Integer duration
    ) {
        resourceService.moveToLocation(resourceId, location, duration);
        return ResponseEntity.accepted().build();
    }

}
