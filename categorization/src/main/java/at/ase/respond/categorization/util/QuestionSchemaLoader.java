package at.ase.respond.categorization.util;

import at.ase.respond.categorization.persistence.questionschema.model.QuestionSchema;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class QuestionSchemaLoader {

    @Value("${custom.questionschema.path}")
    private String questionSchemaPath;

    public QuestionSchema loadQuestionSchema() {
        return loadQuestionSchema(questionSchemaPath);
    }

    private QuestionSchema loadQuestionSchema(String filePath) {
        try (InputStream is = QuestionSchemaLoader.class.getResourceAsStream(filePath)) {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            return mapper.readValue(is, QuestionSchema.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load question schema JSON file: " + filePath, e);
        }
    }
}
