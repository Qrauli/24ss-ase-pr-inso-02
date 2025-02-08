package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.dispatcher.persistence.model.*;
import at.ase.respond.common.dto.ResourceRequestDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResourceRequestMapper {

    @Mapping(target = "assignedIncident", source = "assignedIncident.id")
    @Mapping(target = "resourceId", source = "resource.id")
    ResourceRequestDTO toDTO(ResourceRequest resourceRequest);

}