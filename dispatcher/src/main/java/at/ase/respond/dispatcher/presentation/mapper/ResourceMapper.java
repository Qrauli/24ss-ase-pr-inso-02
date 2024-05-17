package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.ResourceDTO;
import at.ase.respond.dispatcher.persistence.model.Resource;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocationCoordinatesMapper.class})
public interface ResourceMapper {

    @Mapping(target = "assignedIncident", source = "assignedIncident.id")
    ResourceDTO toDTO(Resource resource);

}