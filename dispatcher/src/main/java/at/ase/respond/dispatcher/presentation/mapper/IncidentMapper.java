package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.dispatcher.persistence.model.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    IncidentDTO toDTO(Incident incident);

    @Mapping(target = "version", ignore = true)
    Incident toEntity(IncidentDTO incident);

}
