package at.ase.respond.dispatcher.persistence;

import at.ase.respond.dispatcher.persistence.model.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, UUID> {

}
