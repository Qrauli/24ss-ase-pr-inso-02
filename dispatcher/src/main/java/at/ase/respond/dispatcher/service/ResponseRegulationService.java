package at.ase.respond.dispatcher.service;

import at.ase.respond.common.ResourceType;

import java.util.List;

public interface ResponseRegulationService {

    /**
     * Returns a list of recommended resource types for the incident with the specified code.
     *
     * @param code the code of the incident for which the recommended resource types are to be returned
     * @return a list of recommended resource types
     * @throws IllegalArgumentException if no recommended resource types are found for the specified incident
     */
    List<ResourceType> getRecommendedResourceTypes(String code) throws IllegalArgumentException;
}
