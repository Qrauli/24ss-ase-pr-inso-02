package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.ResourceType;
import at.ase.respond.dispatcher.service.ResponseRegulationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link ResponseRegulationService} interface.
 *
 * <p>Note that we use a service here, because it is possible that
 * the recommended resource types are retrieved from other sources
 * in the future, e.g. another microservice.
 */
@Service
@RequiredArgsConstructor
public class ResponseRegulationServiceImpl implements ResponseRegulationService {

    private final ObjectMapper objectMapper;

    private final ApplicationContext context;

    @Override
    public List<ResourceType> getRecommendedResourceTypes(String code) {
        List<ResourceType> types = new ArrayList<>();

        try {
            String json = context.getResource("classpath:regulations.json").getContentAsString(StandardCharsets.UTF_8);
            JsonNode root = objectMapper.readTree(json);

            // We traverse the whole file - O(n) linear search - but it
            // should be small enough to not cause performance issues
            for (JsonNode node : root) {
                if (node.get("code").asText().equals(code)) {
                    for (JsonNode type : node.get("resources")) {
                        types.add(ResourceType.valueOf(type.asText()));
                    }
                    return types;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read resource-types.json", e);
        }

        throw new IllegalArgumentException("No recommended resource types found for incident with code " + code);
    }

}
