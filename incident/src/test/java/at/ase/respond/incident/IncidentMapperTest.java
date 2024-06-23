package at.ase.respond.incident;

import at.ase.respond.common.Sex;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.incident.persistence.model.Incident;
import at.ase.respond.incident.persistence.model.Location;
import at.ase.respond.incident.persistence.model.LocationAddress;
import at.ase.respond.incident.persistence.model.LocationCoordinates;
import at.ase.respond.incident.persistence.model.Patient;
import at.ase.respond.incident.persistence.model.State;
import at.ase.respond.incident.presentation.dto.IncidentDTO;
import at.ase.respond.incident.presentation.mapper.IncidentMapper;
import at.ase.respond.incident.presentation.mapper.IncidentMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentMapperTest {

    private IncidentMapperImpl mapper;

    @BeforeEach
    public void setUp() {
        mapper = (IncidentMapperImpl) Mappers.getMapper(IncidentMapper.class);
    }

    @Test
    public void testToDTO() {
        // Test with a fully populated Incident
        Location location = Location.builder()
                .coordinates(LocationCoordinates.builder().latitude(1.0).longitude(2.0).build())
                .address(LocationAddress.builder().street("Street").postalCode("12345").city("City").additionalInformation("Info").build())
                .build();
        Patient patient = Patient.builder().age(30).sex(Sex.MALE).build();
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setCode("INC123");
        incident.setState(State.READY);
        incident.setQuestionaryId(UUID.randomUUID());
        incident.setLocation(location);
        incident.setPatients(Collections.singletonList(patient));
        incident.setNumberOfPatients(1);

        IncidentDTO dto = mapper.toDTO(incident);

        assertNotNull(dto);
        assertEquals(incident.getId(), dto.id());
        assertEquals(incident.getCode(), dto.code());
        assertEquals(incident.getState().name(), dto.state());
        assertEquals(incident.getQuestionaryId(), dto.questionaryId());
        assertNotNull(dto.location());
        assertNotNull(dto.patients());
        assertEquals(incident.getNumberOfPatients(), dto.numberOfPatients());

        // Test with a null Incident
        assertNull(mapper.toDTO(null));
    }

    @Test
    public void testToEntity() {
        // Test with a fully populated IncidentDTO
        LocationCoordinatesDTO coordinatesDTO = new LocationCoordinatesDTO(1.0, 2.0);
        LocationAddressDTO addressDTO = new LocationAddressDTO("Street", "12345", "City", "Info");
        LocationDTO locationDTO = new LocationDTO(addressDTO, coordinatesDTO);
        PatientDTO patientDTO = new PatientDTO(30, Sex.MALE);
        IncidentDTO dto = new IncidentDTO(UUID.randomUUID(), List.of(patientDTO), 1, "INC123", locationDTO, UUID.randomUUID(), "READY");

        Incident entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.code(), entity.getCode());
        assertEquals(State.valueOf(dto.state()), entity.getState());
        assertEquals(dto.questionaryId(), entity.getQuestionaryId());
        assertNotNull(entity.getLocation());
        assertNotNull(entity.getPatients());
        assertEquals(dto.numberOfPatients(), entity.getNumberOfPatients());

        // Test with a null IncidentDTO
        assertNull(mapper.toEntity((IncidentDTO) null));
    }

    @Test
    public void testToEventIncident() {
        // Test with a fully populated Incident
        Location location = Location.builder()
                .coordinates(LocationCoordinates.builder().latitude(1.0).longitude(2.0).build())
                .address(LocationAddress.builder().street("Street").postalCode("12345").city("City").additionalInformation("Info").build())
                .build();
        Patient patient = Patient.builder().age(30).sex(Sex.MALE).build();
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setCode("INC123");
        incident.setState(State.READY);
        incident.setQuestionaryId(UUID.randomUUID());
        incident.setLocation(location);
        incident.setPatients(Collections.singletonList(patient));
        incident.setNumberOfPatients(1);

        IncidentCreatedOrUpdatedEvent event = mapper.toEvent(incident);

        assertNotNull(event);
        assertEquals(incident.getId(), event.id());
        assertEquals(incident.getCode(), event.code());
        assertNotNull(event.location());
        assertNotNull(event.patients());
        assertEquals(incident.getNumberOfPatients(), event.numberOfPatients());
        assertNotNull(event.timestamp());

        // Test with a null Incident
        assertNull(mapper.toEvent((Location) null));
    }

    @Test
    public void testLocationToEntity() {
        // Test with a fully populated LocationDTO
        LocationCoordinatesDTO coordinatesDTO = new LocationCoordinatesDTO(1.0, 2.0);
        LocationAddressDTO addressDTO = new LocationAddressDTO("Street", "12345", "City", "Info");
        LocationDTO locationDTO = new LocationDTO(addressDTO, coordinatesDTO);

        Location location = mapper.toEntity(locationDTO);

        assertNotNull(location);
        assertEquals(locationDTO.coordinates().latitude(), location.getCoordinates().getLatitude());
        assertEquals(locationDTO.coordinates().longitude(), location.getCoordinates().getLongitude());
        assertEquals(locationDTO.address().street(), location.getAddress().getStreet());
        assertEquals(locationDTO.address().postalCode(), location.getAddress().getPostalCode());
        assertEquals(locationDTO.address().city(), location.getAddress().getCity());
        assertEquals(locationDTO.address().additionalInformation(), location.getAddress().getAdditionalInformation());

        // Test with a null LocationDTO
        assertNull(mapper.toEntity((LocationDTO) null));
    }

    @Test
    public void testPatientsToEntity() {
        // Test with a fully populated list of PatientDTOs
        PatientDTO patientDTO = new PatientDTO(30, Sex.MALE);
        Collection<Patient> patients = mapper.toEntity(List.of(patientDTO));

        assertNotNull(patients);
        assertEquals(1, patients.size());
        Patient patient = patients.iterator().next();
        assertEquals(patientDTO.age(), patient.getAge());
        assertEquals(patientDTO.sex(), patient.getSex());

        // Test with an empty list of PatientDTOs
        assertTrue(mapper.toEntity(new ArrayList<>()).isEmpty());

        // Test with a null list of PatientDTOs
        assertNull(mapper.toEntity((Collection<PatientDTO>) null));
    }

    @Test
    public void testLocationToEvent() {
        // Test with a fully populated Location
        LocationCoordinates coordinates = LocationCoordinates.builder().latitude(1.0).longitude(2.0).build();
        LocationAddress address = LocationAddress.builder().street("Street").postalCode("12345").city("City").additionalInformation("Info").build();
        Location location = Location.builder().coordinates(coordinates).address(address).build();

        LocationDTO locationDTO = mapper.toEvent(location);

        assertNotNull(locationDTO);
        assertEquals(location.getCoordinates().getLatitude(), locationDTO.coordinates().latitude());
        assertEquals(location.getCoordinates().getLongitude(), locationDTO.coordinates().longitude());
        assertEquals(location.getAddress().getStreet(), locationDTO.address().street());
        assertEquals(location.getAddress().getPostalCode(), locationDTO.address().postalCode());
        assertEquals(location.getAddress().getCity(), locationDTO.address().city());
        assertEquals(location.getAddress().getAdditionalInformation(), locationDTO.address().additionalInformation());

        // Test with a null Location
        assertNull(mapper.toEvent((Location) null));
    }

    @Test
    public void testPatientsToEvent() {
        // Test with a fully populated list of Patients
        Patient patient = Patient.builder().age(30).sex(Sex.MALE).build();
        Collection<PatientDTO> patientDTOs = mapper.toEvent(List.of(patient));

        assertNotNull(patientDTOs);
        assertEquals(1, patientDTOs.size());
        PatientDTO patientDTO = patientDTOs.iterator().next();
        assertEquals(patient.getAge(), patientDTO.age());
        assertEquals(patient.getSex(), patientDTO.sex());

        // Test with an empty list of Patients
        assertTrue(mapper.toEvent(new ArrayList<>()).isEmpty());

        // Test with a null list of Patients
        assertNull(mapper.toEvent((Collection<Patient>) null));
    }

    @Test
    public void testLocationCoordinatesDTOToLocationCoordinates() throws Exception {
        // Test with a fully populated LocationCoordinatesDTO
        LocationCoordinatesDTO coordinatesDTO = new LocationCoordinatesDTO(1.0, 2.0);
        Method method = IncidentMapperImpl.class.getDeclaredMethod("locationCoordinatesDTOToLocationCoordinates", LocationCoordinatesDTO.class);
        method.setAccessible(true);
        LocationCoordinates coordinates = (LocationCoordinates) method.invoke(mapper, coordinatesDTO);

        assertNotNull(coordinates);
        assertEquals(coordinatesDTO.latitude(), coordinates.getLatitude());
        assertEquals(coordinatesDTO.longitude(), coordinates.getLongitude());

        // Test with a null LocationCoordinatesDTO
        assertNull(method.invoke(mapper, (LocationCoordinatesDTO) null));
    }

    @Test
    public void testLocationAddressDTOToLocationAddress() throws Exception {
        // Test with a fully populated LocationAddressDTO
        LocationAddressDTO addressDTO = new LocationAddressDTO("Street", "12345", "City", "Info");
        Method method = IncidentMapperImpl.class.getDeclaredMethod("locationAddressDTOToLocationAddress", LocationAddressDTO.class);
        method.setAccessible(true);
        LocationAddress address = (LocationAddress) method.invoke(mapper, addressDTO);

        assertNotNull(address);
        assertEquals(addressDTO.street(), address.getStreet());
        assertEquals(addressDTO.postalCode(), address.getPostalCode());
        assertEquals(addressDTO.city(), address.getCity());
        assertEquals(addressDTO.additionalInformation(), address.getAdditionalInformation());

        // Test with a null LocationAddressDTO
        assertNull(method.invoke(mapper, (LocationAddressDTO) null));
    }

    @Test
    public void testPatientDTOToPatient() throws Exception {
        // Test with a fully populated PatientDTO
        PatientDTO patientDTO = new PatientDTO(30, Sex.MALE);
        Method method = IncidentMapperImpl.class.getDeclaredMethod("patientDTOToPatient", PatientDTO.class);
        method.setAccessible(true);
        Patient patient = (Patient) method.invoke(mapper, patientDTO);

        assertNotNull(patient);
        assertEquals(patientDTO.age(), patient.getAge());
        assertEquals(patientDTO.sex(), patient.getSex());

        // Test with a null PatientDTO
        assertNull(method.invoke(mapper, (PatientDTO) null));
    }

    @Test
    public void testLocationAddressToLocationAddressDTO() throws Exception {
        // Test with a fully populated LocationAddress
        LocationAddress address = LocationAddress.builder().street("Street").postalCode("12345").city("City").additionalInformation("Info").build();
        Method method = IncidentMapperImpl.class.getDeclaredMethod("locationAddressToLocationAddressDTO", LocationAddress.class);
        method.setAccessible(true);
        LocationAddressDTO addressDTO = (LocationAddressDTO) method.invoke(mapper, address);

        assertNotNull(addressDTO);
        assertEquals(address.getStreet(), addressDTO.street());
        assertEquals(address.getPostalCode(), addressDTO.postalCode());
        assertEquals(address.getCity(), addressDTO.city());
        assertEquals(address.getAdditionalInformation(), addressDTO.additionalInformation());

        // Test with a null LocationAddress
        assertNull(method.invoke(mapper, (LocationAddress) null));
    }

    @Test
    public void testLocationCoordinatesToLocationCoordinatesDTO() throws Exception {
        // Test with a fully populated LocationCoordinates
        LocationCoordinates coordinates = LocationCoordinates.builder().latitude(1.0).longitude(2.0).build();
        Method method = IncidentMapperImpl.class.getDeclaredMethod("locationCoordinatesToLocationCoordinatesDTO", LocationCoordinates.class);
        method.setAccessible(true);
        LocationCoordinatesDTO coordinatesDTO = (LocationCoordinatesDTO) method.invoke(mapper, coordinates);

        assertNotNull(coordinatesDTO);
        assertEquals(coordinates.getLatitude(), coordinatesDTO.latitude());
        assertEquals(coordinates.getLongitude(), coordinatesDTO.longitude());

        // Test with a null LocationCoordinates
        assertNull(method.invoke(mapper, (LocationCoordinates) null));
    }

    @Test
    public void testPatientToPatientDTO() throws Exception {
        // Test with a fully populated Patient
        Patient patient = Patient.builder().age(30).sex(Sex.MALE).build();
        Method method = IncidentMapperImpl.class.getDeclaredMethod("patientToPatientDTO", Patient.class);
        method.setAccessible(true);
        PatientDTO patientDTO = (PatientDTO) method.invoke(mapper, patient);

        assertNotNull(patientDTO);
        assertEquals(patient.getAge(), patientDTO.age());
        assertEquals(patient.getSex(), patientDTO.sex());

        // Test with a null Patient
        assertNull(method.invoke(mapper, (Patient) null));
    }
}
