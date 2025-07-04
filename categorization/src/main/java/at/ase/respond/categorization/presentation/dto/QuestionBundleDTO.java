package at.ase.respond.categorization.presentation.dto;

import at.ase.respond.categorization.presentation.dto.questionschema.BaseQuestionDTO;
import at.ase.respond.categorization.presentation.dto.questionschema.ProtocolQuestionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "QuestionBundleDTO", description = "Data Transfer Object for a categorization")
public class QuestionBundleDTO {

    private BaseQuestionDTO baseQuestion;

    private ProtocolQuestionDTO protocolQuestion;

    private AnswerDTO answer;

}
