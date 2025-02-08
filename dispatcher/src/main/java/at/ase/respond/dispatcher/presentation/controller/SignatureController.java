package at.ase.respond.dispatcher.presentation.controller;


import at.ase.respond.common.dto.LogDTO;
import at.ase.respond.common.logging.LogSignatureException;
import at.ase.respond.common.logging.SignedLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/signatures")
@Slf4j
public class SignatureController {
    private final SignedLogger signedLogger = new SignedLogger();

    @PostMapping("/verify")
    @Operation(summary = "Verifies the signature of logged data, returns true if the signature is valid, false otherwise",
            security = @SecurityRequirement(name = "bearer"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Boolean> verifySignature(@RequestBody LogDTO logDTO) {
        log.info("Verifying signature of log data {}", logDTO);

        Boolean verified;
        try {
            verified = signedLogger.verifySignature(logDTO);
            log.info("Signature verification result: {}", verified);
        } catch (LogSignatureException e) {
            return ResponseEntity.badRequest().body(false);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
        return ResponseEntity.ok(verified);
    }


}
