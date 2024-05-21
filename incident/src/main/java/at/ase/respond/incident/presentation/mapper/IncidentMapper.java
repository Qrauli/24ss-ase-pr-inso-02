package at.ase.respond.incident.presentation.mapper;

import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.persistence.model.Location;
import at.ase.respond.incident.persistence.model.LocationAddress;
import at.ase.respond.incident.persistence.model.LocationCoordinates;
import at.ase.respond.incident.persistence.model.OperationCode;
import at.ase.respond.incident.persistence.model.Patient;
import at.ase.respond.incident.presentation.dto.CategorizationDTO;
import at.ase.respond.incident.presentation.dto.IncidentDTO;

import java.time.ZonedDateTime;
import java.util.Collection;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface IncidentMapper {

    IncidentDTO toDTO(Incident incident);

    default Incident toEntity(IncidentDTO incident) {
        return Incident.builder()
            .id(incident.id())
            .code(OperationCode.from(incident.code()))
            .location(toEntity(incident.location()))
            .patients(toEntity(incident.patients()))
            .numberOfPatients(incident.numberOfPatients())
            .build();
    }

    default IncidentCreatedOrUpdatedEvent toEvent(Incident incident) {
        return new IncidentCreatedOrUpdatedEvent(
                incident.getId(),
                incident.getCode().getCode(),
                toEvent(incident.getLocation()),
                toEvent(incident.getPatients()),
                incident.getNumberOfPatients(),
                ZonedDateTime.now()
        );
    }

    Location toEntity(LocationDTO location);

    Collection<Patient> toEntity(Collection<PatientDTO> patients);

    LocationDTO toEvent(Location location);

    Collection<PatientDTO> toEvent(Collection<Patient> patients);

}
