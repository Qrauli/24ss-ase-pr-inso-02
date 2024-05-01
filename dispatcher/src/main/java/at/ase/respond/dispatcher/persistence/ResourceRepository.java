package at.ase.respond.dispatcher.persistence;

import at.ase.respond.dispatcher.persistence.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {

}