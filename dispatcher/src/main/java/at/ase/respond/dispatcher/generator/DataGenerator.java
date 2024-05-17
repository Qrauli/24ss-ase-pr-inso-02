package at.ase.respond.dispatcher.generator;

import at.ase.respond.dispatcher.persistence.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.Resource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
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
        resourceRepository.saveAll(getResources());
        log.info("Generated presentation data");
    }

    private List<Resource> getResources() {
        List<Resource> resources;
        try {
            resources = objectMapper.readValue(
                    context.getResource("classpath:resources.json").getInputStream(),
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read resources.json", e);
        }
        return resources;
    }

}
