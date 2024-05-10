package at.ase.respond.datafeeder.presentation.controller;

import at.ase.respond.datafeeder.presentation.dto.ResourceDTO;
import at.ase.respond.datafeeder.presentation.dto.ResourceState;
import at.ase.respond.datafeeder.presentation.dto.ResourceType;
import at.ase.respond.datafeeder.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

	private final ResourceService resourceService;

	@PostMapping(value = "/{resourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates a new resource")
	public ResponseEntity<ResourceDTO> createResource(@PathVariable String resourceId, @RequestParam ResourceType type,
			@RequestParam Double latitude, @RequestParam Double longitude) {
		try {
			ResourceDTO createdResource = resourceService.create(resourceId, type, latitude, longitude);
			return ResponseEntity.ok(createdResource);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping(value = "/{resourceId}/state", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates a resource")
	public ResponseEntity<ResourceDTO> updateResource(@PathVariable String resourceId,
			@RequestParam ResourceState state) {
		try {
			ResourceDTO updatedResource = resourceService.updateState(resourceId, state);
			return ResponseEntity.ok(updatedResource);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value = "/{resourceId}/location", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates the location of a resource")
	public ResponseEntity<?> updateResourceLocation(@PathVariable String resourceId, @RequestParam Double latitude,
			@RequestParam Double longitude) {
		try {
			resourceService.updateLocation(resourceId, latitude, longitude);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok().build();
	}

	@DeleteMapping(value = "/{resourceId}")
	@Operation(summary = "Deletes a resource")
	public ResponseEntity<?> deleteResource(@PathVariable String resourceId) {
		try {
			resourceService.delete(resourceId);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok().build();
	}

}
