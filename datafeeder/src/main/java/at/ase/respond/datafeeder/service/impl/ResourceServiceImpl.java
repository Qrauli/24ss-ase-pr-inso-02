package at.ase.respond.datafeeder.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.ResourceDTO;
import at.ase.respond.common.event.ResourceLocationUpdatedEvent;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.datafeeder.service.MessageSender;
import at.ase.respond.datafeeder.service.ResourceService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final Environment environment;

    private final ObjectMapper objectMapper;

    private final ApplicationContext context;

    private final MessageSender messageSender;

    private final Map<String, ResourceDTO> resources = new ConcurrentHashMap<>();

    @PostConstruct
    private void generateData() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("presentation")) {
            log.info("Generating presentation data...");

            List<ResourceDTO> newResources;
            try {
                newResources = objectMapper.readValue(
                        context.getResource("classpath:resources.json").getInputStream(),
                        new TypeReference<>() {}
                );
            } catch (IOException e) {
                throw new UncheckedIOException("Could not read resources.json", e);
            }

            newResources.forEach(resource -> resources.put(resource.id(), resource));

            log.info("Generated presentation data");
        }
    }

    @Override
    @Synchronized("resources")
    public List<ResourceDTO> findAll() {
        return List.copyOf(resources.values());
    }

    @Override
    @Synchronized("resources")
    public ResourceDTO updateState(String resourceId, ResourceState newState) {
        ResourceDTO newResource = resources.computeIfPresent(resourceId, (id, resource) -> {
            ResourceDTO updatedResource = new ResourceDTO(
                    resource.id(),
                    resource.type(),
                    newState,
                    resource.locationCoordinates(),
                    resource.assignedIncident(),
                    ZonedDateTime.now()
            );
            resources.put(id, updatedResource);

            ResourceStatusUpdatedEvent event = new ResourceStatusUpdatedEvent(
                    resourceId,
                    newState,
                    ZonedDateTime.now()
            );
            messageSender.publish(event);

            return updatedResource;
        });

        if (newResource != null) {
            return newResource;
        }

        throw new NotFoundException("Resource " + resourceId + " not found");
    }

    @Override
    @Synchronized("resources")
    public ResourceDTO updateLocation(String resourceId, LocationCoordinatesDTO newLocation) {
        ResourceDTO newResource = resources.computeIfPresent(resourceId, (id, resource) -> {
            ResourceDTO updatedResource = new ResourceDTO(
                    resource.id(),
                    resource.type(),
                    resource.state(),
                    newLocation,
                    resource.assignedIncident(),
                    ZonedDateTime.now()
            );
            resources.put(id, updatedResource);

            ResourceLocationUpdatedEvent event = new ResourceLocationUpdatedEvent(
                    resourceId,
                    newLocation,
                    ZonedDateTime.now()
            );
            messageSender.publish(event);

            return updatedResource;
        });

        if (newResource != null) {
            return newResource;
        }

        throw new NotFoundException("Resource " + resourceId + " not found");
    }

    @Override
    @Async
    public void moveToLocation(String resourceId, LocationCoordinatesDTO newLocation, Integer duration) throws NotFoundException {
        log.debug("Moving resource {} to location {} in {} seconds", resourceId, newLocation, duration);
        LocationCoordinatesDTO currentLocation = resources.get(resourceId).locationCoordinates();
        double latitudeDelta = (newLocation.latitude() - currentLocation.latitude()) / duration;
        double longitudeDelta = (newLocation.longitude() - currentLocation.longitude()) / duration;
        for (int i = 0; i <= duration; i++) {
            try {
                LocationCoordinatesDTO intermediateLocation = new LocationCoordinatesDTO(
                        currentLocation.latitude() + latitudeDelta * i,
                        currentLocation.longitude() + longitudeDelta * i
                );
                updateLocation(resourceId, intermediateLocation);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        log.debug("Resource {} moved to location {}", resourceId, newLocation);
    }

}
