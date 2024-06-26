package at.ase.respond.dispatcher.persistence.repository.impl;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.repository.ResourceCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResourceCustomRepositoryImpl implements ResourceCustomRepository {

    private final MongoTemplate mongoTemplate;

    public List<GeoResult<Resource>> findRecommendedResources(GeoJsonPoint incidentLocation, ResourceType resourceType) {
        Criteria criteria = Criteria.where("state").in(ResourceState.AVAILABLE)
                .and("type").is(resourceType);

        NearQuery query = NearQuery.near(incidentLocation, Metrics.KILOMETERS)
                .spherical(true)
                .limit(1)
                .query(Query.query(criteria));

        return mongoTemplate.geoNear(query, Resource.class).getContent();
    }
}
