package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.dispatcher.persistence.vo.PatientVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO toDTO(PatientVO patient);

    PatientVO toVO(PatientDTO patient);
}
