package at.ase.respond.dispatcher.persistence.repository;

import at.ase.respond.common.ResourceType;
import at.ase.respond.dispatcher.persistence.model.Resource;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

public interface ResourceCustomRepository {

    /**
     * Returns an ordered list of the top 10 recommended resources for the incident at the specified location.
     * Note that those resources are either available or dispatched.
     *
     * @param incidentLocation the location of the incident
     * @param resourceType     the type of the resources to be recommended
     * @return a list of recommended resources for the incident at the specified location and of the specified type
     */
    List<GeoResult<Resource>> findRecommendedResources(GeoJsonPoint incidentLocation, ResourceType resourceType);
}
