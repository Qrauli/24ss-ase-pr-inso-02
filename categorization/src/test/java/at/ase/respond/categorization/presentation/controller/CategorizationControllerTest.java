package at.ase.respond.categorization.presentation.controller;

import at.ase.respond.categorization.persistence.questionschema.model.FieldType;
import at.ase.respond.categorization.persistence.questionschema.model.QuestionType;
import at.ase.respond.categorization.presentation.dto.AnswerDTO;
import at.ase.respond.categorization.presentation.dto.CategorizationDTO;
import at.ase.respond.categorization.presentation.dto.QuestionBundleDTO;
import at.ase.respond.categorization.presentation.dto.questionschema.BaseQuestionDTO;
import at.ase.respond.categorization.presentation.dto.questionschema.FieldDTO;
import at.ase.respond.categorization.service.CategorizationService;
import at.ase.respond.categorization.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategorizationController.class)
public class CategorizationControllerTest {

    private static final String ENDPOINT_URL = "/categorization";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategorizationService categorizationService;

    @Test
    void whenNoJWT_createSession_then403Forbidden() throws Exception {
        mvc.perform(post(ENDPOINT_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenJWT_createSession_then200Ok() throws Exception {
        final CategorizationDTO categorizationDTO = createDummyCategorizationDTOWithoutAnswer(UUID.randomUUID());
        when(categorizationService.createSession(any(String.class))).thenReturn(categorizationDTO);

        MvcResult result = mvc.perform(post(ENDPOINT_URL)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("user")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(categorizationDTO)))
                .andReturn();
        assertEquals(categorizationDTO, new ObjectMapper().readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), CategorizationDTO.class));
    }

    @Test
    void whenNoJWT_saveAnswer_then403Forbidden() throws Exception {
        final AnswerDTO answerDTO = createDummyAnswerDTO();
        mvc.perform(put(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenJWT_saveAnswer_then200Ok() throws Exception {
        final UUID sessionId = UUID.randomUUID();
        final AnswerDTO answerDTO = createDummyAnswerDTO();
        final CategorizationDTO answeredCategorizationDTO = createDummyCategorizationDTOWithAnswers(sessionId);
        when(categorizationService.save(eq(sessionId), any())).thenReturn(answeredCategorizationDTO);

        MvcResult result = mvc.perform(put(ENDPOINT_URL + "/" + sessionId)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("user")))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(answeredCategorizationDTO)))
                .andReturn();
        assertEquals(answeredCategorizationDTO, new ObjectMapper().readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), CategorizationDTO.class));
    }

    @Test
    void whenNoJWT_findById_then401Unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJWTNoSession_findById_then404NotFound() throws Exception {
        when(categorizationService.findById(any())).thenThrow(NotFoundException.class);

        mvc.perform(get(ENDPOINT_URL + "/00000000-0000-0000-0000-000000000000")
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("user")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenJWT_findById_then200Ok() throws Exception {
        final UUID sessionId = UUID.randomUUID();
        final CategorizationDTO categorizationDTO = createDummyCategorizationDTOWithoutAnswer(sessionId);
        when(categorizationService.findById(eq(sessionId))).thenReturn(categorizationDTO);

        MvcResult result = mvc.perform(get(ENDPOINT_URL + "/" + sessionId)
                        .with(jwt().jwt(jwt -> jwt.claim("realm_access", Map.of("roles", List.of("user")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(categorizationDTO)))
                .andReturn();
        assertEquals(categorizationDTO, new ObjectMapper().readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), CategorizationDTO.class));
    }

    private CategorizationDTO createDummyCategorizationDTOWithoutAnswer(UUID sessionId) {
        final List<FieldDTO> fields = List.of(
                new FieldDTO("city", "Stadt", FieldType.TEXT, List.of()),
                new FieldDTO("street", "Straße", FieldType.TEXT, List.of()),
                new FieldDTO("postalCode", "PostalCode", FieldType.TEXT, List.of()),
                new FieldDTO("lat", "Latitude", FieldType.TEXT, List.of()),
                new FieldDTO("lon", "Longitude", FieldType.TEXT, List.of()),
                new FieldDTO("additionalData", "Zusatzinformationen", FieldType.TEXT, List.of()),
                new FieldDTO("name", "Name", FieldType.TEXT, List.of()),
                new FieldDTO("phone", "Telefonnummer", FieldType.TEXT, List.of()),
                new FieldDTO("numberOfPeople", "Anzahl", FieldType.NUMBER, List.of()),
                new FieldDTO("age", "Alter", FieldType.NUMBER, List.of()),
                new FieldDTO("gender", "Geschlecht", FieldType.SINGLE_CHOICE, List.of("Male", "Female", "Unknown"))
        );

        final BaseQuestionDTO baseQuestion = new BaseQuestionDTO(QuestionType.BASE, 1, "Wo ist der Unfallort?", fields);
        final QuestionBundleDTO questionBundle = new QuestionBundleDTO(baseQuestion, null, null);

        return new CategorizationDTO(sessionId, List.of(questionBundle), "admin", LocalDateTime.now().toString(), null);
    }

    private CategorizationDTO createDummyCategorizationDTOWithAnswers(UUID sessionId) {
        final List<FieldDTO> fields = List.of(
                new FieldDTO("city", "Stadt", FieldType.TEXT, List.of()),
                new FieldDTO("street", "Straße", FieldType.TEXT, List.of()),
                new FieldDTO("postalCode", "PostalCode", FieldType.TEXT, List.of()),
                new FieldDTO("lat", "Latitude", FieldType.TEXT, List.of()),
                new FieldDTO("lon", "Longitude", FieldType.TEXT, List.of()),
                new FieldDTO("additionalData", "Zusatzinformationen", FieldType.TEXT, List.of()),
                new FieldDTO("name", "Name", FieldType.TEXT, List.of()),
                new FieldDTO("phone", "Telefonnummer", FieldType.TEXT, List.of()),
                new FieldDTO("numberOfPeople", "Anzahl", FieldType.NUMBER, List.of()),
                new FieldDTO("age", "Alter", FieldType.NUMBER, List.of()),
                new FieldDTO("gender", "Geschlecht", FieldType.SINGLE_CHOICE, List.of("Male", "Female", "Unknown"))
        );

        final BaseQuestionDTO baseQuestion = new BaseQuestionDTO(QuestionType.BASE, 1, "Wo ist der Unfallort?", fields);
        final QuestionBundleDTO questionBundle = new QuestionBundleDTO(baseQuestion, null, createDummyAnswerDTO());

        return new CategorizationDTO(sessionId, List.of(questionBundle), "admin", LocalDateTime.now().toString(), null);
    }

    private AnswerDTO createDummyAnswerDTO() {
        Map<String, String> answers = new HashMap<>();
        answers.put("city", "Vienna");
        answers.put("street", "Karlsplatz 4");
        answers.put("postalCode", "1010");
        answers.put("lat", "48.1995");
        answers.put("lon", "16.3699");
        answers.put("additionalData", "Additional information");
        answers.put("name", "John Doe");
        answers.put("phone", "123456789");
        answers.put("numberOfPeople", "2");
        answers.put("age", "30");
        answers.put("gender", "Male");

        return new AnswerDTO(QuestionType.BASE, 1, 0, answers);
    }
}
