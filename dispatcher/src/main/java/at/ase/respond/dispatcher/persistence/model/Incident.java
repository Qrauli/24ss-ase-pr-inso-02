package at.ase.respond.dispatcher.persistence.model;

import at.ase.respond.common.IncidentState;
import at.ase.respond.dispatcher.persistence.vo.LocationVO;
import at.ase.respond.dispatcher.persistence.vo.PatientVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "incidents")
public class Incident {

    @Id
    private UUID id;

    private String code;

    private IncidentState state;

    private LocationVO location;

    private Integer numberOfPatients;

    @Builder.Default
    private Collection<PatientVO> patients = new ArrayList<>();

    @Builder.Default
    private Collection<String> assignedResources = new ArrayList<>();

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @Version
    private Integer version;

}
