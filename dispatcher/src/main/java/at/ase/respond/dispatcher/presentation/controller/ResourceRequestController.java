package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.dispatcher.presentation.mapper.ResourceRequestMapper;
import at.ase.respond.common.dto.ResourceRequestDTO;
import at.ase.respond.dispatcher.service.ResourceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ResourceRequestController {

    private final ResourceRequestService service;

    private final ResourceRequestMapper resourceRequestMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of requests returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Returns a list of all requests")
    public ResponseEntity<List<ResourceRequestDTO>> findAll(
            @RequestParam(value = "open", required = false, defaultValue = "true") boolean open
    ) {
        return ResponseEntity.ok(service.findAll(open).stream().map(resourceRequestMapper::toDTO).toList());
    }

    @PutMapping(value = "/{id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request state updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @Operation(summary = "Finish resource request")
    @CrossOrigin
    public ResponseEntity<ResourceRequestDTO> finishRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(resourceRequestMapper.toDTO(service.finishRequest(id)));
    }

}
