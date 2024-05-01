package at.ase.respond.dispatcher.service;

import at.ase.respond.dispatcher.persistence.model.Resource;

import java.util.List;
import java.util.UUID;

public interface ResourceService {

    List<Resource> findAll();

    Resource assignToIncident(String resourceId, UUID incidentId);

}