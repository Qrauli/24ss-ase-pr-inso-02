package at.ase.respond.datafeeder.service;
import at.ase.respond.common.ResourceType;

public interface ResourceRequestService {

    /**
     * Creates a new resource request
     * @param resourceId the id of the resource that requested additional resources
     * @param requestedResourceType the type of the resource that was requested
     */
    public void create(String resourceId, ResourceType requestedResourceType);
}
