package at.ase.respond.dispatcher.presentation.controller;

import at.ase.respond.dispatcher.presentation.IncidentMapper;
import at.ase.respond.dispatcher.presentation.dto.IncidentDTO;
import at.ase.respond.dispatcher.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of all incidents")
    public ResponseEntity<List<IncidentDTO>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(IncidentMapper::toDTO)
                        .toList()
        );
    }
}
