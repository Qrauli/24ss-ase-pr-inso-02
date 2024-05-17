package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.dispatcher.persistence.vo.LocationVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LocationAddressMapper.class, LocationCoordinatesMapper.class})
public interface LocationMapper {

    LocationDTO toDTO(LocationVO location);

    LocationVO toVO(LocationDTO location);

}
