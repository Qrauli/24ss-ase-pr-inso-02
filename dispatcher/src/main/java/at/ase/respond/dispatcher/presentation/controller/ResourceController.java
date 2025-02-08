package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.common.dto.ResourceDTO;
import at.ase.respond.dispatcher.presentation.mapper.ResourceMapper;
import at.ase.respond.dispatcher.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService service;

    private final ResourceMapper mapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of resources returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(
            summary = "Returns a list of all resources",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<ResourceDTO>> findAll(
            @RequestParam(value = "additional", required = false, defaultValue = "false") boolean additional,
            Principal principal
    ) {
        MDC.put("user", principal.getName());
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDTO).toList());
    }

    @PostMapping(value = "/{resourceId}/assign/{incidentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resource assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Resource not available"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Resource or incident not found")
    })
    @Operation(
            summary = "Assigns a resource to an incident",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<ResourceDTO> assignToIncident(
            @PathVariable String resourceId,
            @PathVariable UUID incidentId,
            Principal principal
    ) {
        MDC.put("user", principal.getName());
            return ResponseEntity.ok(mapper.toDTO(service.assignToIncident(resourceId, incidentId)));
    }

}
