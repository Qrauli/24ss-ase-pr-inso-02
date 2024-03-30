package at.ase.respond.categorizer.presentation.controller;

import at.ase.respond.categorizer.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    @PostMapping
    public ResponseEntity<UUID> create() {
        return ResponseEntity.ok(service.create());
    }
}
