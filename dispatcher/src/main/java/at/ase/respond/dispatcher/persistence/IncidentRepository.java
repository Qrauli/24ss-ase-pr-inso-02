package at.ase.respond.dispatcher.persistence;

import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.common.IncidentState;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, UUID> {

    /**
     * find all incidents which are not in the specified state
     * @param state the state of the incidents to exclude
     * @return a list of all incidents which are not in the specified state
     */
    List<Incident> findByStateNot(IncidentState state);

}
