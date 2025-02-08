package at.ase.respond.dispatcher.persistence.repository;

import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.common.IncidentState;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, UUID> {

    /**
     * Finds all incidents which are not in the specified state.
     *
     * @param state the state of the incidents to be excluded
     * @return a list of all incidents which are not in the specified state
     */
    List<Incident> findByStateNot(IncidentState state);

}
