package at.ase.respond.dispatcher.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.ResourceType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "requests")
public class ResourceRequest {

    @Id
    private UUID id;

    private ResourceRequestState state;

    private ResourceType requestedResourceType;

    @DocumentReference(collection = "incidents")
    private Incident assignedIncident;

    @DocumentReference(collection = "resources")
    private Resource resource;

    @CreatedDate
    private ZonedDateTime createdAt;

}