package com.make.equo.application.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.make.equo.application.addon.EquoWebSocketServerAddon;
import com.make.equo.application.util.IConstants;

public class ParameterizedCommandHandler {

	@Execute
	public void execute(@Named("commandId") String commandId,
			@Named(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT) String userEvent, MApplication mApplication,
			EModelService modelService) {
		Runnable runnable = (Runnable) mApplication.getTransientData().get(commandId);

		if (runnable != null) {
			runnable.run();
		}

		if (userEvent != null) {
			List<MAddon> webSocketServerAddons = modelService.findElements(mApplication,
					IConstants.EQUO_WEBSOCKET_SERVER_ADDON, MAddon.class, null);
			if (!webSocketServerAddons.isEmpty()) {
				MAddon webSocketServerAddon = webSocketServerAddons.get(0);
				EquoWebSocketServerAddon equoWebSocketServerAddon = (EquoWebSocketServerAddon) webSocketServerAddon
						.getObject();
				equoWebSocketServerAddon.getWebSocketServer().broadcast(userEvent);
			}
		}
	}

}
