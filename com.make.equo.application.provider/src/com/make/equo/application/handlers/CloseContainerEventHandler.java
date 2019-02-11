package com.make.equo.application.handlers;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class CloseContainerEventHandler implements EventHandler {

	private IEventBroker broker;
	@SuppressWarnings("rawtypes")
	private MElementContainer elementContainer;

	public CloseContainerEventHandler(@SuppressWarnings("rawtypes") MElementContainer elementContainer, IEventBroker broker) {
		this.elementContainer = elementContainer;
		this.broker = broker;
	}

	@Override
	public void handleEvent(Event event) {
		if (elementContainer.getChildren().size() == 0) {
			MElementContainer<MUIElement> parent = elementContainer.getParent();
			if (parent != null) {
				parent.getChildren().remove(elementContainer);
				parent.setToBeRendered(true);
				broker.unsubscribe(this);
			}
		}
	}
}
