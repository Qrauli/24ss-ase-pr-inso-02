package at.ase.respond.datafeeder.service.impl;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import at.ase.respond.common.ResourceType;
import at.ase.respond.common.event.AdditionalResourcesRequestedEvent;
import at.ase.respond.datafeeder.service.MessageSender;
import at.ase.respond.datafeeder.service.ResourceRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceRequestServiceImpl implements ResourceRequestService {

    private final MessageSender messageSender;

    @Override
    public void create(String resourceId, ResourceType requestedResourceType) {
        AdditionalResourcesRequestedEvent additionalResourceRequestedEvent =
                new AdditionalResourcesRequestedEvent(resourceId, requestedResourceType, ZonedDateTime.now());
        messageSender.publish(additionalResourceRequestedEvent);
    }
}
