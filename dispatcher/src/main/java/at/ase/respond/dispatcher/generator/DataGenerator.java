package at.ase.respond.dispatcher.generator;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.dispatcher.persistence.repository.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.Resource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("presentation")
public class DataGenerator {

    private final ObjectMapper objectMapper;

    private final ApplicationContext context;

    private final ResourceRepository resourceRepository;

    @PostConstruct
    private void generateData() {
        if (resourceRepository.count() != 0) {
            log.info("Presentation data already exists, skipping generation");
            return;
        }

        log.info("Generating presentation data...");
        resourceRepository.saveAll(parseResources());
        log.info("Generated presentation data");
    }

    private List<Resource> parseResources() {
        List<Resource> resources = new ArrayList<>();

        try {
            String json = context.getResource("classpath:resources.json").getContentAsString(StandardCharsets.UTF_8);
            JsonNode root = objectMapper.readTree(json);

            // Traverse the JSON tree
            for (JsonNode node : root) {
                resources.add(parseResource(node));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read resources.json", e);
        }

        return resources;
    }

    private Resource parseResource(JsonNode node) {
        Resource resource = new Resource();

        resource.setId(node.get("id").asText());
        resource.setType(ResourceType.valueOf(node.get("type").asText()));
        resource.setState(ResourceState.valueOf(node.get("state").asText()));

        JsonNode locationNode = node.get("locationCoordinates");
        double lon = locationNode.get("longitude").asDouble();
        double lat = locationNode.get("latitude").asDouble();
        resource.setLocationCoordinates(new GeoJsonPoint(lon, lat));

        return resource;
    }

}
