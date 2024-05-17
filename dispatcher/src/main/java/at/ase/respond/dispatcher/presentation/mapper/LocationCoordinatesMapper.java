package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.dispatcher.persistence.vo.LocationCoordinatesVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationCoordinatesMapper {

    LocationCoordinatesDTO toDTO(LocationCoordinatesVO locationCoordinates);

    LocationCoordinatesVO toVO(LocationCoordinatesDTO locationCoordinates);

}
