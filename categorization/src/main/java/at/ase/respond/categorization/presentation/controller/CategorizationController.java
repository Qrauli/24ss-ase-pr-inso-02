package at.ase.respond.categorization.presentation.controller;

import at.ase.respond.categorization.presentation.dto.AnswerDTO;
import at.ase.respond.categorization.presentation.dto.CategorizationDTO;
import at.ase.respond.categorization.service.CategorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategorizationController {

    private final CategorizationService service;

    @PostMapping(value = "/categorization", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Creates a new session in the categorization service and returns the an initialized categorization object with the first base question and the sessionId.",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<CategorizationDTO> createSession(Principal principal) {
        MDC.put("user", principal.getName());
        log.info("Creating a new categorization session.");
        CategorizationDTO categorization = service.createSession(principal.getName());
        log.info("Session created with sessionId: {}", categorization.sessionID());
        return ResponseEntity.ok(categorization);
    }

    @PutMapping(value = "/categorization/{sessionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Saves an answer to a question and returns the updated categorization object.",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<CategorizationDTO> saveAnswer(@PathVariable UUID sessionId, @RequestBody AnswerDTO answer, Principal principal) {
        MDC.put("user", principal.getName());
        log.info("Saving answer for session with sessionId: {}", sessionId);
        CategorizationDTO categorization = service.save(sessionId, answer);
        log.info("Answer saved for session with sessionId: {}", sessionId);
        return ResponseEntity.ok(categorization);
    }

    @GetMapping(value = "/categorization/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Returns the categorization object with the given categorization sessionId.",
            security = @SecurityRequirement(name = "bearer")
    )
    public ResponseEntity<CategorizationDTO> findById(@PathVariable UUID sessionId, Principal principal) {
        MDC.put("user", principal.getName());
        log.info("Finding categorization session with sessionId: {}", sessionId);
        return ResponseEntity.ok(service.findById(sessionId));
    }

}
