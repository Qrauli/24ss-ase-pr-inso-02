package at.ase.respond.datafeeder.service.impl;

import at.ase.respond.datafeeder.config.RabbitConfig;
import at.ase.respond.datafeeder.presentation.dto.*;
import at.ase.respond.datafeeder.service.MessageSender;
import at.ase.respond.datafeeder.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static at.ase.respond.datafeeder.presentation.dto.ResourceState.AVAILABLE;
import static at.ase.respond.datafeeder.presentation.dto.ResourceState.UNAVAILABLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

	private final RabbitTemplate rabbitTemplate;

	private final RabbitConfig rabbitConfig;

	private final TopicExchange exchange;

	private final MessageSender messageSender;

	private final Map<String, ResourceDTO> resources = new HashMap<>();

	@Override
	public ResourceDTO create(String resourceId, ResourceType type, Double latitude, Double longitude) {
		LocationCoordinatesDTO locationCoordinates = new LocationCoordinatesDTO(latitude, longitude);
		ResourceDTO resourceDTO = new ResourceDTO(resourceId, type, AVAILABLE, locationCoordinates, null);
		if (resources.containsKey(resourceId)) {
			throw new IllegalArgumentException("Resource with id " + resourceId + " already exists");
		}

		ResourceStatusUpdatedEvent resourceStatusUpdatedEvent = new ResourceStatusUpdatedEvent(OffsetDateTime.now(), resourceId, type,
				AVAILABLE, null);
		messageSender.send(resourceStatusUpdatedEvent);
		resources.put(resourceId, resourceDTO);

		ResourceLocationUpdatedEvent resourceLocationUpdatedEvent = new ResourceLocationUpdatedEvent(resourceId,
				locationCoordinates);
		messageSender.send(resourceLocationUpdatedEvent);

		return resourceDTO;
	}

	@Override
	public ResourceDTO updateState(String resourceId, ResourceState state) {
		ResourceDTO resourceDTO = resources.get(resourceId);
		if (resourceDTO == null) {
			throw new IllegalArgumentException("Resource with id " + resourceId + " not found");
		}
		ResourceStatusUpdatedEvent event = new ResourceStatusUpdatedEvent(OffsetDateTime.now(), resourceDTO.id(), null, state,
				resourceDTO.assignedIncident());
		messageSender.send(event);
		ResourceDTO updatedResource = new ResourceDTO(resourceDTO.id(), resourceDTO.type(), state,
				resourceDTO.locationCoordinates(), resourceDTO.assignedIncident());
		resources.put(resourceId, updatedResource);
		return updatedResource;
	}

	@Override
	public void updateLocation(String resourceId, Double latitude, Double longitude) {
		ResourceLocationUpdatedEvent event = new ResourceLocationUpdatedEvent(resourceId,
				new LocationCoordinatesDTO(latitude, longitude));
		messageSender.send(event);
	}

	@Override
	public void delete(String resourceId) {
		if (!resources.containsKey(resourceId)) {
			throw new IllegalArgumentException("Resource with id " + resourceId + " not found");
		}
		ResourceStatusUpdatedEvent event = new ResourceStatusUpdatedEvent(OffsetDateTime.now(), resourceId, resources.get(resourceId).type(),
				UNAVAILABLE, null);
		messageSender.send(event);

		resources.remove(resourceId);
	}

}
