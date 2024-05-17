package at.ase.respond.dispatcher.persistence.model;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;

import at.ase.respond.dispatcher.persistence.vo.LocationCoordinatesVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resources")
public class Resource {

    @Id
    private String id;

    private ResourceType type;

    private ResourceState state;

    private LocationCoordinatesVO locationCoordinates;

    private ZonedDateTime updatedAt;

    @DocumentReference(collection = "incidents")
    private Incident assignedIncident;

}