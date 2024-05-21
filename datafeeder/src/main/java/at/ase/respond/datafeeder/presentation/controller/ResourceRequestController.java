package at.ase.respond.datafeeder.presentation.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import at.ase.respond.common.ResourceType;
import at.ase.respond.datafeeder.service.ResourceRequestService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ResourceRequestController {

    private final ResourceRequestService resourceRequestService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a new resource request")
    public ResponseEntity<Void> createResourceRequest(@RequestParam String resourceId, @RequestParam ResourceType requestedResourceType) {
        resourceRequestService.create(resourceId, requestedResourceType);
        return ResponseEntity.ok().build();
    }
}
