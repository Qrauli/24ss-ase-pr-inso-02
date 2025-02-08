package at.ase.respond.dispatcher.service;

import at.ase.respond.common.ResourceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResponseRegulationServiceTest {

    @Autowired
    private ResponseRegulationService service;

    @Test
    void whenNoLegalCode_getRecommendedResourceTypes_thenThrowIllegalArgumentException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.getRecommendedResourceTypes(null)
        );
    }

    @Test
    void whenLegalCode_getRecommendedResourceTypes_returnResourceType() {
        assertThat(service.getRecommendedResourceTypes("01A01")).containsExactly(ResourceType.KTW);
    }

}