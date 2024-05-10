package at.ase.respond.datafeeder.service;

import at.ase.respond.datafeeder.presentation.dto.ResourceLocationUpdatedEvent;
import at.ase.respond.datafeeder.presentation.dto.ResourceStatusUpdatedEvent;

public interface MessageSender {

	void send(ResourceStatusUpdatedEvent payload);

	void send(ResourceLocationUpdatedEvent payload);

}
