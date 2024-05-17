package at.ase.respond.dispatcher.presentation.mapper;

import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.dispatcher.persistence.vo.LocationAddressVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationAddressMapper {

    LocationAddressDTO toDTO(LocationAddressVO address);

    LocationAddressVO toVO(LocationAddressDTO address);
}
