package at.ase.respond.incident.presentation.mapper;

import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.persistence.model.Location;
import at.ase.respond.incident.persistence.model.LocationAddress;
import at.ase.respond.incident.persistence.model.LocationCoordinates;
import at.ase.respond.incident.persistence.model.OperationCode;
import at.ase.respond.incident.persistence.model.Patient;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.dto.LocationAddressDTO;
import at.ase.respond.incident.presentation.dto.LocationCoordinatesDTO;
import at.ase.respond.incident.presentation.dto.LocationDTO;
import at.ase.respond.incident.presentation.dto.PatientDTO;
import at.ase.respond.incident.presentation.event.IncidentCreatedEvent;

import java.util.Collection;

public final class IncidentMapper {

    private IncidentMapper() {
        throw new AssertionError("Static Class - Do not instantiate!");
    }

    public static Incident toEntity(IncidentDTO incident) {
        return Incident.builder()
            .id(incident.id())
            .code(OperationCode.from(incident.categorization().code()))
            .location(toEntity(incident.location()))
            .patients(toEntity(incident.patients()))
            .numberOfPatients(incident.numberOfPatients())
            .build();
    }

    public static IncidentCreatedEvent toEvent(Incident incident) {
        return new IncidentCreatedEvent(incident.getId(), incident.getCode().getCode(), toEvent(incident.getLocation()),
                toEvent(incident.getPatients()), incident.getNumberOfPatients());
    }

    private static Location toEntity(LocationDTO location) {
        Double lat = location.coordinates().latitude();
        Double lon = location.coordinates().longitude();

        String street = location.description().street();
        String postalCode = location.description().postalCode();
        String city = location.description().city();
        String additionalInformation = location.description().additionalInformation();

        return Location.builder()
            .coordinates(new LocationCoordinates(lat, lon))
            .address(new LocationAddress(street, postalCode, city, additionalInformation))
            .build();
    }

    private static Collection<Patient> toEntity(Collection<PatientDTO> patients) {
        return patients.stream().map(p -> new Patient(p.age(), p.sex())).toList();
    }

    private static LocationDTO toEvent(Location location) {
        Double lat = location.getCoordinates().getLatitude();
        Double lon = location.getCoordinates().getLongitude();

        String street = location.getAddress().getStreet();
        String postalCode = location.getAddress().getPostalCode();
        String city = location.getAddress().getCity();
        String additionalInformation = location.getAddress().getAdditionalInformation();

        LocationAddressDTO address = new LocationAddressDTO(street, postalCode, city, additionalInformation);
        LocationCoordinatesDTO coordinates = new LocationCoordinatesDTO(lat, lon);
        return new LocationDTO(address, coordinates);
    }

    private static Collection<PatientDTO> toEvent(Collection<Patient> patients) {
        return patients.stream().map(p -> new PatientDTO(p.getAge(), p.getSex())).toList();
    }

}
