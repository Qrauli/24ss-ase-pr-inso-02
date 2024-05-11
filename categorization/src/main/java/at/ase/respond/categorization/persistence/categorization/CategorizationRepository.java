package at.ase.respond.categorization.persistence.categorization;

import at.ase.respond.categorization.persistence.categorization.model.Categorization;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CategorizationRepository extends MongoRepository<Categorization, UUID> {

}
