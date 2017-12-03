package com.make.equo.application.addon;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.model.application.MApplication;

public class EquoWebSocketServerAddon {

	private EquoWebSocketServer webSocketServer;

	public static final String CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.addon.EquoWebSocketServerAddon";

	@PostConstruct
	public void init(MApplication mApplication, ECommandService commandService, EHandlerService handlerService) {
		this.webSocketServer = createWebSocketServer(commandService, handlerService);
	}

	private EquoWebSocketServer createWebSocketServer(ECommandService commandService, EHandlerService handlerService) {
		EquoWebSocketServer equoWebSocketServer = EquoWebSocketServer.startServer(commandService, handlerService);
		return equoWebSocketServer;
	}

	@PreDestroy
	public void stop() {
		if (webSocketServer != null) {
			try {
				webSocketServer.stop();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public EquoWebSocketServer getWebSocketServer() {
		return webSocketServer;
	}

}
