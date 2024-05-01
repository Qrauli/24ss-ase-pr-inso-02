package at.ase.respond.dispatcher.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resources")
public class Resource {

    @Id
    private String id;

    private ResourceType type;

    private LocationCoordinates locationCoordinates;

    @DocumentReference(collection = "incidents")
    private Incident assignedIncident;

}