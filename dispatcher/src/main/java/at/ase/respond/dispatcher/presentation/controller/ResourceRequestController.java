package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.dispatcher.presentation.mapper.ResourceRequestMapper;
import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.common.dto.ResourceRequestDTO;
import at.ase.respond.dispatcher.service.ResourceRequestService;
import at.ase.respond.dispatcher.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "Returns a list of all requests")
    public ResponseEntity<List<ResourceRequestDTO>> findAll(
            @RequestParam(value = "open", required = false, defaultValue = "true") boolean additional) {
        return ResponseEntity.ok(service.findAll(additional).stream().map(resourceRequestMapper::toDTO).toList());
    }

    @PutMapping(value = "/{id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finish resource request")
    @CrossOrigin
    public ResponseEntity<ResourceRequestDTO> finishRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(resourceRequestMapper.toDTO(service.finishRequest(id)));
    }

}
