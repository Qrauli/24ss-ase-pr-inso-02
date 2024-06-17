package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.dispatcher.persistence.model.Incident;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface IncidentMapper {

    IncidentDTO toDTO(Incident incident);

}
