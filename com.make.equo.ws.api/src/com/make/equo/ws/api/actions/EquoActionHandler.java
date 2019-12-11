package com.make.equo.ws.api.actions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.ws.api.IEquoWebSocketService;


@Component
public abstract class EquoActionHandler implements IActionHandler {

	private static final long serialVersionUID = 1L;
	
	private IEquoWebSocketService webSocketService;

	@Activate
	public void start() {
		webSocketService.addActionHandler(this.getClass().getSimpleName().toLowerCase(), this);
	}
	
	
	@Reference
	public void setWebSocketService(IEquoWebSocketService webSocketService) {
		this.webSocketService = webSocketService;
	}
	
	public void unsetWebSocketService(IEquoWebSocketService webSocketService) {
		this.webSocketService = null;
	}
}
